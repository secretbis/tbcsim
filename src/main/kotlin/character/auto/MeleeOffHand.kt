package character.auto

import character.Ability

class MeleeOffHand() : Ability() {
    var lastAttack: Int = -1

    override val name: String = "Melee (OH)"

    override fun available(): Boolean {
        return true
    }

    override fun cast() {
        TODO("Not yet implemented")
    }
}
