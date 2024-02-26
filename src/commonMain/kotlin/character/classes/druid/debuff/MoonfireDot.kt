package character.classes.druid.debuff

import character.Ability
import character.Debuff
import character.Proc
import character.classes.druid.talents.ImprovedMoonfire
import character.classes.druid.talents.Moonfury
import data.Constants
import data.itemsets.CorruptorRaiment
import data.itemsets.VoidheartRaiment
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType

import sim.SimParticipant
import kotlin.reflect.KProperty

class MoonfireDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Moonfire (DoT)"
    }
    override val name: String = Companion.name
    override val durationMs: Int = 12000
    override val tickDeltaMs: Int = 3000

    val moonfireDot = object : Ability() {
        override val id: Int = 26988
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 150.0
        val numTicks = durationMs / tickDeltaMs
        val school = Constants.DamageType.ARCANE
        override fun cast(sp: SimParticipant) {
            val spellPowerCoeff = 0.1302

            val impMoonfire = sp.getTalent<ImprovedMoonfire>(ImprovedMoonfire.name)
            val impMoonfireIncreasedDamagePercent = impMoonfire?.increasedMoonfireDamagePercent() ?: 0.0

            val moonfury = sp.getTalent<Moonfury>(Moonfury.name)
            val moonFuryIncreasedDamagePercent = moonfury?.increasedDamagePercent() ?: 0.0

            val damageMulti = 1.0 + impMoonfireIncreasedDamagePercent + moonFuryIncreasedDamagePercent
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff) * damageMulti

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = EventResult.HIT,
            )
            owner.logEvent(event)
        }
}
    override fun tick(sp: SimParticipant) {
        moonfireDot.cast(sp)
    }
}
