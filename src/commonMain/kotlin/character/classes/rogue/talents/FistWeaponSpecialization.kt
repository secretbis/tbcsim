package character.classes.rogue.talents

import character.*
import mechanics.Rating
import sim.SimParticipant

class FistWeaponSpecialization(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Fist Weapon Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun critIncrease(): Double {
        return currentRank * 0.01
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val icon: String = "inv_gauntlets_04.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                fistWeaponAdditionalCritChancePercent = critIncrease()
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
