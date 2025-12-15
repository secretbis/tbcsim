package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import character.Proc
import data.Constants
import data.itemsets.CorruptorRaiment
import data.itemsets.VoidheartRaiment
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType

import sim.SimParticipant
import kotlin.reflect.KProperty

class ImmolateDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Immolate (DoT)"
    }
    override val name: String = Companion.name
    override val icon: String = "spell_fire_immolation.jpg"
    override val durationMs: Int by object : Any() {
        operator fun getValue(dot: ImmolateDot, property: KProperty<*>): Int {
            val baseDuration = 15000

            // Check T4 bonus
            val t4Bonus = owner.buffs[VoidheartRaiment.FOUR_SET_BUFF_NAME] != null
            val t4BonusDuration = if(t4Bonus) { VoidheartRaiment.fourSetIncreasedDotDurationMs() } else 0

            return baseDuration + t4BonusDuration
        }
    }
    override val tickDeltaMs: Int = 3000

    val immolateDot = object : Ability() {
        override val id: Int = 27215
        override val name: String = Companion.name
        override val icon: String = "spell_fire_immolation.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 41.0
        val school = Constants.DamageType.FIRE
        val snapshotSpellPower = owner.stats.getSpellDamage(school)
        val spellPowerCoeff = 0.13

        // Check T5 bonus
        val t5Bonus = owner.buffs[CorruptorRaiment.FOUR_SET_BUFF_NAME] != null
        val t5BonusMultiplier = if(t5Bonus) { CorruptorRaiment.fourSetDotDamageIncreaseMultiplier() } else 1.0

        override fun cast(sp: SimParticipant) {
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff, snapshotSpellPower) * t5BonusMultiplier

            // Each tick can still resist partially
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

            owner.fireProc(listOf(Proc.Trigger.FIRE_DAMAGE_PERIODIC), listOf(), this, event)
        }
}
    override fun tick(sp: SimParticipant) {
        immolateDot.cast(sp)
    }
}
