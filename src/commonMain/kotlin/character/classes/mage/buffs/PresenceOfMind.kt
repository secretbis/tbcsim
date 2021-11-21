package character.classes.mage.buffs

import character.Buff

class PresenceOfMind : Buff() {
    companion object {
        const val name = "Presence of Mind"
    }

    override val name: String = Companion.name
    override val icon: String = "spell_nature_enchantarmor.jpg"
    override val durationMs: Int = -1
}
