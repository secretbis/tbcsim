package character.classes.priest.debuffs

import character.classes.priest.talents.*
import character.Ability
import character.Debuff
import character.Proc
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType

import sim.SimParticipant

class ShadowWordPainDot(owner: SimParticipant) : Debuff(owner) {
    override val name: String = "Shadow Word Pain (DoT)"
    override val durationMs: Int = getDuration(owner)
    override val tickDeltaMs: Int = 3000

    fun getDuration(sp: SimParticipant): Int {
        val impSWP: ImprovedShadowWordPain? = sp.character.klass.talentInstance(ImprovedShadowWordPain.name)
        val durationIncrease = impSWP?.swpDurationIncrease() ?: 0
        return 18000 + durationIncrease
    }

    val swpdAbility = object : Ability() {
        override val id: Int = 10894
        override val name: String = "Shadow Word: Pain (DoT)"
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 206.0
        val numTicks = 6.0
        val school = Constants.DamageType.SHADOW
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

            owner.fireProc(listOf(Proc.Trigger.FIRE_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        swpdAbility.cast(sp)
    }
}
