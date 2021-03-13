package character.classes.shaman.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class MentalQuickness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Mental Quickness"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    override fun buffs(sp: SimParticipant): List<Buff> {
        return listOf(
            object : Buff() {
                override val name: String = "Mental Quickness (Spell Damage)"
                override val durationMs: Int = -1
                override val hidden: Boolean = true

                override fun modifyStats(sp: SimParticipant): Stats {
                    val modifier = currentRank * 0.1
                    val spellDamage = modifier * sp.attackPower()
                    return Stats(spellDamage = spellDamage.toInt())
                }
            }
        )
    }

    fun instantManaCostReduction(): Double {
        return when(currentRank) {
            1 -> 0.02
            2 -> 0.04
            3 -> 0.06
            else -> 0.0
        }
    }
}
