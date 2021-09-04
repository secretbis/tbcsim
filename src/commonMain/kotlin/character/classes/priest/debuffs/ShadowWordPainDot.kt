package character.classes.priest.debuffs

import character.Ability
import character.Debuff
import character.Buff
import character.Proc
import character.Resource
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class ShadowWordPainDot(owner: SimParticipant, damageRoll: Double, tickCount: Int) : Debuff(owner) {
    companion object {
        const val name: String = "Shadow Word Pain (DoT)"
    }

    override val name: String = Companion.name
    override val tickDeltaMs: Int = 3000
    override val durationMs: Int = tickCount * tickDeltaMs
    
    var baseTickCount = 6
    val school = Constants.DamageType.SHADOW
    var baseDamage = damageRoll / baseTickCount

    val ability = object : Ability() {
        override val id: Int = 25368
        override val name: String = Companion.name

        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val spellMultiplier = sp.stats.getSpellDamageTakenMultiplier(school)
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
        ability.cast(sp)
    }
}
