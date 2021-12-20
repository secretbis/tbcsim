package data.buffs

import character.Buff

// Implementations of shot mechanics should check for this buff and exclude ammo damage
class NoAmmo : Buff() {
    companion object {
        const val name = "No Ammo"
    }

    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val hidden: Boolean = true
}