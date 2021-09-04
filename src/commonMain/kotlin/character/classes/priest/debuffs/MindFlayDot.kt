package character.classes.priest.debuffs

import character.Ability
import character.Debuff
import data.Constants
import data.model.Item
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant
import character.Proc

class MindFlayDot(owner: SimParticipant, val damageRoll: Double, ticks: Int) : Debuff(owner) {
    companion object {
        const val name = "Mind Flay (DoT)"
    }
    override val name: String = Companion.name
    override val durationMs = ticks.coerceAtLeast(1).coerceAtMost(3) * 1000
    override val tickDeltaMs: Int = durationMs / ticks

    val school = Constants.DamageType.SHADOW

    val mindFlay = object : Ability() {
        override val id: Int = 25387
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val spellMultiplier = sp.stats.getSpellDamageTakenMultiplier(school)
            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll * spellMultiplier,
                result = EventResult.HIT,
            )
            owner.logEvent(event)
            owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        mindFlay.cast(sp)
    }
}
