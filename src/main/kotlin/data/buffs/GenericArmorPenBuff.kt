package data.buffs

import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class GenericArmorPenBuff(val armorPen: Int): Buff() {
    override val name: String = "Armor Penetration $armorPen"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(armorPen = armorPen))
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
