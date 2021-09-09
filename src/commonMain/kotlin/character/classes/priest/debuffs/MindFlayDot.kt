package character.classes.priest.debuffs

import character.Ability
import character.Debuff
import data.Constants
import data.itemsets.IncarnateRegalia
import data.model.Item
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant
import character.Proc

class MindFlayDot(owner: SimParticipant, ticks: Int) : Debuff(owner) {
    companion object {
        const val name = "Mind Flay (DoT)"
    }
    override val id = 25387
    override val name: String = Companion.name
    override val durationMs = (ticks.coerceAtLeast(1).coerceAtMost(3) * 1000 / owner.spellHasteMultiplier()).toInt()
    override val tickDeltaMs: Int = durationMs / ticks

    val school = Constants.DamageType.SHADOW
    val snapShotSpellPower = owner.spellDamageWithSchool(school).toDouble() 
    var baseDotDamage: Double = 176.0
    val baseDotSpellCoeff = 0.19

    val ability = object : Ability() {
        override val id: Int = 25387
        override val name: String = Companion.name

        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val t4FourSetBonusMulti: Double = if(owner.buffs[IncarnateRegalia.FOUR_SET_BUFF_NAME] == null) 1.0 else 1.05

            val damageRoll: Double = Spell.baseDamageRollFromSnapShot(baseDotDamage, snapShotSpellPower, baseDotSpellCoeff)
            val result = Spell.attackRoll(
                owner, 
                damageRoll,
                school,
                bonusDamageMultiplier = t4FourSetBonusMulti, 
                canCrit = false,
                canResist = false,
            )

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = result.first,
                result = result.second
            )
            owner.logEvent(event)

            owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        ability.cast(sp)
    }
}
