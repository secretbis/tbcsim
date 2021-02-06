package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.SimIteration

class TwoHandWeaponSpec(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Two-Handed Weapon Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return if(Melee.is2H(sim.subject.gear.mainHand)) {
                val multiplier = currentRank * 0.01
                Stats(physicalDamageMultiplier = 1.0 + multiplier)
            } else Stats()
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
