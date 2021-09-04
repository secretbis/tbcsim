package character.classes.priest.abilities

import character.classes.priest.talents.FocusedMind
import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.classes.priest.talents.MindFlay as MindFlayTalent
import character.Ability
import character.Buff
import character.Proc
import character.classes.priest.debuffs.*
import character.Resource
import data.Constants
import data.model.Item
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant
import mu.KotlinLogging

abstract class MindFlay : Ability() {
    override val id: Int = 25387
    open val tickCount = 3

    var baseDamage = 528.0
    val school = Constants.DamageType.SHADOW
    // See https://www.warcrafttavern.com/tbc/guides/shadow-priest-damage-coefficients/
    val spellPowerCoeff = 0.57

    // TODO: Move to talent instead of re-applying each cast
    val interruptWatch = object : Buff(){
        override val name = "Mind Flay (Static)"
        override val hidden = true
        override val durationMs = -1

        // In general only the GCD prevents another spell from being cast for channeled spells
        // When another spell is cast, mind flay "dot" should stop ticking
        val interruptProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_START_CAST,
                Trigger.SPELL_CAST
            )
            override val type: Type = Type.STATIC
    
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                // Mind Flay will refresh the dot if cast again, so we skip removing it
                if (ability == null || ability.name.startsWith("Mind Flay")) return

                val dot = sp.sim.target.debuffs.get(MindFlayDot.name)

                if (dot != null ){
                    sp.sim.target.consumeDebuff(dot);
                }
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(interruptProc)
    }

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseResourceCost = 230.0
    override fun resourceCost(sp: SimParticipant): Double {
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?

        if (innerFocusBuff != null) {
            return 0.0
        }

        val fm: FocusedMind? = sp.character.klass.talentInstance(FocusedMind.name)
        val fmMulti = fm?.manaReductionMultiplier() ?: 1.0

        return baseResourceCost * fmMulti
    }

    override fun available(sp: SimParticipant): Boolean {
        return super.available(sp) && sp.character.klass.hasTalentRanks(MindFlayTalent.name)
    }

    override fun cast(sp: SimParticipant) {
        // Overall damage is determined before doing tick damage
        val damageRoll = Spell.baseDamageRollSingle(sp, baseDamage, school, spellPowerCoeff)
        val resultTick = Spell.attackRoll(
            sp, 
            damageRoll,
            school,
            isBinary = true,
            canCrit = false,
        )
        val initialCast = Event(
            eventType = EventType.SPELL_CAST,
            damageType = school,
            abilityName = name,
            result = resultTick.second,
        )

        sp.logEvent(initialCast);

        if (resultTick.first == 0.0){
            sp.fireProc(listOf(Proc.Trigger.SPELL_RESIST), listOf(), this, initialCast)
            return;
        }

        // Initial cast needs to have a chance to proc Shadow Weaving, etc. Subsequent damage is considered periodic
        sp.fireProc(listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC), listOf(), this, initialCast)

        sp.addBuff(interruptWatch)
        sp.sim.target.addDebuff(MindFlayDot(sp, resultTick.first / 3, tickCount));
    }
}
