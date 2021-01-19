package character.classes.shaman.buffs

import character.Buff
import character.Ability
import character.Stats
import sim.Sim

class UnleashedRage : Buff() {
    override var appliedAtMs: Int = -1
    override val durationMs: Int = 10000
    override val statModType: ModType = ModType.PERCENTAGE
    override val hidden: Boolean = false

    override fun modifyStats(sim: Sim, stats: Stats): Stats {
        TODO("Not yet implemented")
    }
}
