package character.classes.priest.buffs

import character.Buff

class InnerFocus : Buff() {
    companion object {
        const val name = "Inner Focus"
    }

    override val name: String = Companion.name
    override val durationMs: Int = -1

    fun critPct(): Double = 0.25
}
