package character.classes.mage.talents

import character.Talent

class ImprovedFireBlast(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Fire Blast"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun fireBlastCooldownReductionMs(): Int = currentRank * 500
}
