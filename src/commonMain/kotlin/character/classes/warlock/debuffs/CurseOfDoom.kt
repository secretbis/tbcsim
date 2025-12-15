package character.classes.warlock.debuffs

import character.Ability
import character.Buff
import character.Debuff
import character.Proc
import character.classes.warlock.abilities.AmplifyCurse
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class CurseOfDoom(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Curse of Doom"
    }

    override val name: String = Companion.name
    override val icon: String = "spell_shadow_auraofdarkness.jpg"
    override val durationMs: Int = 60000
    override val tickDeltaMs: Int = 60000

    val doom = object : Ability() {
        override val id: Int = 27216
        override val name: String = Companion.name
        override val icon: String = "spell_shadow_auraofdarkness.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 4200.0
        val school = Constants.DamageType.SHADOW
        val snapshotSpellPower = owner.stats.getSpellDamage(school)
        val spellPowerCoeff = 2.0

        // Amplify Curse
        val ampCurseMultiplier = if(owner.buffs[AmplifyCurse.name] != null) { 1.5 } else 1.0
        init {
            owner.consumeBuff(object : Buff() {
                override val name: String = AmplifyCurse.name
                override val durationMs: Int = -1
            })
        }

        override fun cast(sp: SimParticipant) {
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff, snapshotSpellPower) * ampCurseMultiplier

            // The end tick can still resist partially
            val result = Spell.partialResistRoll(
                owner,
                Pair(damageRoll, EventResult.HIT),
                school
            )

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                ability = this,
                amount = result.first,
                result = result.second
            )
            owner.logEvent(event)

            owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        doom.cast(sp)
    }
}
