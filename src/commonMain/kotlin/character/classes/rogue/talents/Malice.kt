package character.classes.rogue.talents

import character.*
import sim.SimParticipant
import mechanics.Rating

class Malice(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Malice"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun critChancePercentIncrease(): Double {
        return currentRank * 1.0
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val icon: String = "ability_racial_bloodrage.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val critRating = Rating.critPerPct * critChancePercentIncrease()
            return Stats(
                meleeCritRating = critRating
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
