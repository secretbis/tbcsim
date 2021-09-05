package character.races.debuffs

import character.Ability
import character.Debuff
import character.Buff
import character.Proc
import character.Resource
import character.classes.priest.talents.ShadowWeaving
import character.classes.priest.talents.VampiricTouch
import character.classes.priest.debuffs.ShadowWeavingDebuff
import character.classes.priest.debuffs.VampiricTouchDot
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class DevouringPlagueDot(owner: SimParticipant, damageRoll: Double, tickCount: Int) : Debuff(owner) {
    companion object {
        const val name: String = "Devouring Plague (DoT)"
    }

    override val name: String = Companion.name
    override val tickDeltaMs: Int = 3000
    override val durationMs: Int = tickCount * tickDeltaMs

    val school = Constants.DamageType.SHADOW
    val baseTickCount = 8
    val baseDamage = damageRoll / baseTickCount

    val dpAbility = object : Ability() {
        override val id: Int = 25467
        override val name: String = Companion.name

        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val spellMultiplier = sp.stats.getSpellDamageTakenMultiplier(school)
            sp.sim.logger.debug{"Plague dot ${damageRoll} ${spellMultiplier}"}
            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = baseDamage * spellMultiplier,
                result = EventResult.HIT
            )
            owner.logEvent(event)

            owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        dpAbility.cast(sp)
    }
}
