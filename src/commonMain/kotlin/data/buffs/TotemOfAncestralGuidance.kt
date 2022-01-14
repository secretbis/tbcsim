package data.buffs

import character.Buff

class TotemOfAncestralGuidance : Buff() {
    companion object {
        const val name = "Totem of Ancestral Guidance"
    }

    override val id: Int = 41040
    override val name: String = Companion.name
    override val icon: String = "spell_nature_giftofthewaterspirit.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    fun lightningDamageBonus(): Int {
        return 85
    }
}
