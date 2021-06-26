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

class StarshardsDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name: String = "Starshards (DoT)"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 15000
    override val tickDeltaMs: Int = 3000

    val starshardsAbility = object : Ability() {
        override val id: Int = 19305
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 130.0
        val numTicks = 5.0
        val school = Constants.DamageType.ARCANE
        override fun cast(sp: SimParticipant) {
            val spellPowerCoeff = Spell.spellPowerCoeff(0, durationMs) / numTicks

            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff)

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
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
