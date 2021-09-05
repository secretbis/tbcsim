package character.races.debuffs

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

class StarshardsDot(owner: SimParticipant, damageRoll: Double, tickCount: Int) : Debuff(owner) {
    companion object {
        const val name: String = "Starshards (DoT)"
    }

    override val name: String = Companion.name
    override val tickDeltaMs: Int = 3000
    override val durationMs: Int = tickCount * tickDeltaMs

    val school = Constants.DamageType.ARCANE
    val baseTickCount = 5
    val baseDamage = damageRoll / baseTickCount

    val starshardsAbility = object : Ability() {
        override val id: Int = 25446
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

            owner.fireProc(listOf(Proc.Trigger.ARCANE_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        starshardsAbility.cast(sp)
    }
}
