package character.classes.priest.debuffs

import character.Ability
import character.Debuff
import character.Proc
import data.Constants
import data.itemsets.IncarnateRegalia
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class MindFlayDot(owner: SimParticipant, ticks: Int) : Debuff(owner) {
    companion object {
        const val name = "Mind Flay (DoT)"
    }

    override val id = 25387
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_siphonmana.jpg"
    override val durationMs = (ticks.coerceAtLeast(1).coerceAtMost(3) * 1000 / owner.spellHasteMultiplier()).toInt()
    override val tickDeltaMs: Int = durationMs / ticks

    val t4FourSetBonusMulti: Double = if (owner.buffs[IncarnateRegalia.FOUR_SET_BUFF_NAME] == null) 1.0 else 1.05
    val school = Constants.DamageType.SHADOW
    val snapShotSpellPower = owner.spellDamageWithSchool(school)
    var baseDotDamage: Double = 176.0
    val baseDotSpellCoeff = 0.19

    val ability =
        object : Ability() {
            override val id: Int = 25387
            override val name: String = Companion.name
            override val icon: String = "spell_shadow_siphonmana.jpg"

            override fun gcdMs(sp: SimParticipant): Int = 0

            override fun cast(sp: SimParticipant) {
                val damageRoll: Double =
                    Spell.baseDamageRollSingle(
                        owner,
                        baseDotDamage,
                        school,
                        baseDotSpellCoeff,
                        snapShotSpellPower,
                        t4FourSetBonusMulti,
                    )

                // Per testing, mind flay ticks actually cannot resist, so just send hit events for
                // each
                val event =
                    Event(
                        eventType = EventType.DAMAGE,
                        damageType = school,
                        ability = this,
                        amount = damageRoll,
                        result = EventResult.HIT,
                    )
                owner.logEvent(event)

                owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_PERIODIC), listOf(), this, event)
            }
        }

    override fun tick(sp: SimParticipant) {
        ability.cast(sp)
    }
}
