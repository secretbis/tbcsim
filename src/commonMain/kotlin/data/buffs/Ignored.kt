package data.buffs

import character.Buff

class Ignored(detail: String) : Buff() {
    override val name: String = "Ignored Buff (${detail})"
    override val durationMs: Int = -1
}