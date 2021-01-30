package data.default

import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class UnknownBuff : Buff() {
    override val name: String = "Unknown"
    override val durationMs: Int = -1
}
