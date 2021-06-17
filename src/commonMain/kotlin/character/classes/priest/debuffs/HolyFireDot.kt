package character.classes.priest.debuffs

import character.Ability
import character.Debuff
import character.Proc
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class HolyFireDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Holy Fire (DoT)"
    }
    override val name: String = Companion.name
    override val durationMs: Int = 10000
    override val tickDeltaMs: Int = 2000

    val hfDotAbility = object : Ability() {
        override val id: Int = 25384
        override val name: String = Companion.name

        override fun castTimeMs(sp: SimParticipant): Int = 0
        override fun gcdMs(sp: SimParticipant): Int = 0
        override val castableOnGcd: Boolean = true

        val dmgPerTick = 33.0
        val school = Constants.DamageType.HOLY
        override fun cast(sp: SimParticipant) {
            val spellPowerCoeff = 0.033
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff)

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = EventResult.HIT
            )
            owner.logEvent(event)

            owner.fireProc(listOf(Proc.Trigger.HOLY_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        hfDotAbility.cast(sp)
    }
}
