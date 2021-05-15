package character.classes.mage.abilities

import character.Ability
import character.Buff
import character.Proc
import character.classes.mage.buffs.ArcanePower
import character.classes.mage.buffs.PresenceOfMind
import character.classes.mage.talents.ArcaneFocus
import character.classes.mage.talents.ArcaneImpact
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimParticipant

class ArcaneBlast : Ability() {
    companion object {
        const val name: String = "Arcane Blast"
    }
    override val id: Int = 30451
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCastTimeMs = 2500
    override fun castTimeMs(sp: SimParticipant): Int {
        val pomBuff = sp.buffs[PresenceOfMind.name] as PresenceOfMind?
        return if(pomBuff != null) {
            sp.consumeBuff(pomBuff)
            0
        } else {
            val abBuff = sp.buffs[Companion.name] as ArcaneBlastBuff?
            return baseCastTimeMs - (abBuff?.castTimeReductionMs(sp) ?: 0)
        }
    }

    val baseResourceCost = 195.0
    override fun resourceCost(sp: SimParticipant): Double {
        val abBuff = sp.buffs[Companion.name] as ArcaneBlastBuff?
        val abMult = abBuff?.manaCostMultiplier(sp) ?: 1.0

        val apBuff = sp.buffs[ArcanePower.name] as ArcanePower?
        val apMult = apBuff?.manaCostMultiplier() ?: 1.0

        // From testing, these stack based on the base mana cost
        return baseResourceCost * abMult + baseResourceCost * apMult
    }

    class ArcaneBlastBuff : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 8000
        override val hidden: Boolean = true
        override val maxStacks: Int = 3

        fun manaCostMultiplier(sp: SimParticipant): Double {
            val state = state(sp)
            return 1.0 + (0.75 * state.currentStacks)
        }

        fun castTimeReductionMs(sp: SimParticipant): Int {
            val state = state(sp)
            return when(state.currentStacks) {
                1 -> 300
                2 -> 700
                3 -> 1000
                else -> 0
            }
        }
    }

    val baseDamage = Pair(668.0, 772.0)
    val school = Constants.DamageType.ARCANE
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
    override fun cast(sp: SimParticipant) {
        val arcaneFocus: ArcaneFocus? = sp.character.klass.talentInstance(ArcaneFocus.name)
        val afHit = arcaneFocus?.arcaneAddlSpellHitPct() ?: 0.0

        val arcaneImpact: ArcaneImpact? = sp.character.klass.talentInstance(ArcaneImpact.name)
        val aiCrit = arcaneImpact?.arcaneBlastExplosionAddlCritChance() ?: 0.0

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff)
        val result = Spell.attackRoll(sp, damageRoll, school, bonusCritChance = aiCrit, bonusHitChance = afHit)

         val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Proc anything that can proc off non-periodic Shadow damage
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.ARCANE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.ARCANE_DAMAGE)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.ARCANE_DAMAGE)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.ARCANE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
