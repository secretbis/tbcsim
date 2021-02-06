package data.buffs.permanent

import character.Buff
import character.Stats

abstract class PermanentBuff : Buff() {
    override val durationMs = -1

    // These are stats that never need a SimIteration context to know the value
    // Gear stats implemented as buffs like "Attack Power 12" and stat enchants fit this category well
    // This field is currently only read by Gear, since abilities almost always have talents or procs that need
    //   to be accounted for dynamically
    abstract fun permanentStats(): Stats
}
