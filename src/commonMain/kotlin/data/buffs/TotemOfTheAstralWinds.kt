package data.buffs

import character.Buff

class TotemOfTheAstralWinds : Buff() {
    companion object {
        const val name = "Totem of the Astral Winds"
    }

    override val id: Int = 34244
    override val name: String = Companion.name
    override val icon: String = "spell_unused.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    fun windfuryWeaponApBonus(): Int {
        return 80
    }
}
