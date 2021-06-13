package character.classes.mage.debuffs

import character.Ability
import character.Debuff
import character.Proc
import data.Constants
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class FireballDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Fireball (DoT)"
    }
    override val name: String = Companion.name
    override val durationMs: Int = 8000
    override val tickDeltaMs: Int = 2000

    val dmgPerTick = 21.0
    val fbDotAbility = object : Ability() {
        override val id: Int = 38692
        override val name: String = Companion.name

        override fun castTimeMs(sp: SimParticipant): Int = 0
        override fun gcdMs(sp: SimParticipant): Int = 0
        override val castableOnGcd: Boolean = true

        override fun cast(sp: SimParticipant) {
            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = Constants.DamageType.FIRE,
                abilityName = name,
                amount = dmgPerTick,
                result = EventResult.HIT,
            )
            owner.logEvent(event)

            val triggerTypes = listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE_PERIODIC)
            owner.fireProc(triggerTypes, listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        fbDotAbility.cast(sp)
    }
}
