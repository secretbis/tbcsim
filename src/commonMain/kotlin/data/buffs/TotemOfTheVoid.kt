package data.buffs

import character.Buff

class TotemOfTheVoid : Buff() {
    companion object {
        const val name = "Totem of the Void"
    }

    override val id: Int = 34230
    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    fun lightningDamageBonus(): Int {
        return 55
    }
}
