package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import data.Constants
import data.model.Item
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

        // TODO: This might be cleaner as a new 2H-specific stat
        private fun is2H(item: Item): Boolean {
            return item.itemSubclass == Constants.ItemSubclass.SWORD_2H ||
                   item.itemSubclass == Constants.ItemSubclass.AXE_2H ||
                   item.itemSubclass == Constants.ItemSubclass.MACE_2H ||
                   item.itemSubclass == Constants.ItemSubclass.POLEARM ||
                   // TECHNICALLY YOU COULD DO THIS, TECHNICALLY
                   item.itemSubclass == Constants.ItemSubclass.STAFF
        }

        override fun modifyStats(sim: SimIteration): Stats {
            return if(is2H(sim.subject.gear.mainHand)) {
                val multiplier = currentRank * 0.01
                Stats(physicalDamageMultiplier = multiplier)
            } else Stats()
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
