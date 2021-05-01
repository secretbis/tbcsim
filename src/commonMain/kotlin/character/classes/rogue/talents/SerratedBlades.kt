package character.classes.rogue.talents

import character.*
import mechanics.Rating
import sim.SimParticipant

class SerratedBlades(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Serrated Blades"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun decreasedArmor(): Int {
        return when(currentRank) {
            1 -> 186
            2 -> 373
            3 -> 560
            else -> 0
        }
    }

    fun increasedDamagePercent(): Double {
        return currentRank * 10.0
    }

    fun getDebuff(sp: SimParticipant): Debuff {
        return object : Debuff(sp) {
            override val name: String = "${Companion.name} (Talent)"
            override val durationMs: Int = -1
            override val hidden: Boolean = true
    
            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(
                    armor = -1 * decreasedArmor()
                )
            }
        }
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun refresh(sp: SimParticipant) {
            super.refresh(sp)
            sp.sim.target.addDebuff(getDebuff(sp))
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
