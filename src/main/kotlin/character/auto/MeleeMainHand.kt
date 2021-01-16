package character.auto

import character.Ability

class MeleeMainHand() : Ability() {
    var lastAttack: Int = -1

    override val name: String = "Melee (MH)"

    override fun available(): Boolean {
        return true
    }

    override fun cast() {
        TODO("Not yet implemented")
    }
}
