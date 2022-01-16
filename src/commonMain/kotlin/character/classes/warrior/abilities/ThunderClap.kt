package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.FocusedRage
import character.classes.warrior.talents.ImprovedThunderClap
import data.Constants
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class ThunderClap : Ability() {
    companion object {
        const val name: String = "Thunder Clap"
    }

    override val id: Int = 25264
    override val name: String = Companion.name
    override val icon: String = "spell_nature_thunderclap.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        val validStance = sp.buffs[BattleStance.name] != null || sp.buffs[DefensiveStance.name] != null
        return validStance && super.available(sp)
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double {
        val focusedRageRanks = sp.character.klass.talentRanks(FocusedRage.name)
        val impTclapRanks = sp.character.klass.talentRanks(ImprovedThunderClap.name)
        val impTclapReduction = when(impTclapRanks) {
            3 -> 4
            2 -> 2
            1 -> 1
            else -> 0
        }

        return 20.0 - focusedRageRanks - impTclapReduction
    }

    override fun cast(sp: SimParticipant) {
        val debuff = object : Debuff(sp) {
            override val name: String = Companion.name
            override val icon: String = "spell_nature_thunderclap.jpg"
            override val durationMs: Int = 120000

            override fun modifyStats(sp: SimParticipant): Stats {
                val impTclapRanks = sp.character.klass.talentRanks(ImprovedThunderClap.name)
                val impTclapMod = when(impTclapRanks) {
                    3 -> 0.1
                    2 -> 0.7
                    1 -> 0.4
                    else -> 0.0
                }

                return Stats(
                    physicalHasteMultiplier = 0.9 - impTclapMod
                )
            }
        }

        sp.sim.target.addDebuff(debuff)

        val impTclapRanks = sp.character.klass.talentRanks(ImprovedThunderClap.name)
        val baseDamage = 123.0
        val impTclapMult = when(impTclapRanks) {
            3 -> 2.0
            2 -> 1.7
            1 -> 1.4
            else -> 1.0
        }

        sp.logEvent(
            Event(
                eventType = EventType.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                amount = baseDamage * impTclapMult,
                result = EventResult.HIT,
                abilityThreatMultiplier = 1.75
            )
        )
    }
}
