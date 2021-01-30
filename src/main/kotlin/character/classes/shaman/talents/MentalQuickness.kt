package character.classes.shaman.talents

import character.Buff
import character.Proc
import character.Stats
import character.Talent
import sim.SimIteration

class MentalQuickness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Mental Quickness"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    override fun buffs(sim: SimIteration): List<Buff> {
        return listOf(
            object : Buff() {
                override val name: String = "${Companion.name} (Spell Damage)"
                override val durationMs: Int = -1
                override val hidden: Boolean = true

                override fun modifyStats(sim: SimIteration): Stats {
                    val modifier = currentRank * 0.1
                    val spellDamage = modifier * sim.attackPower()
                    return Stats(spellDamage = spellDamage.toInt())
                }
            }
        )
    }

    // TODO: Apply this discount to instant spells
    fun instantManaCostMultiplier(): Double {
        return when(currentRank) {
            0 -> 1.0
            1 -> 1.13
            2 -> 1.26
            else -> 1.4
        }
    }
}
