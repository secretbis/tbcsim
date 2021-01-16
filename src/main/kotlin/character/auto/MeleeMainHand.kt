package character.auto

import character.Character
import character.Ability

class MeleeMainHand(character: Character) : Ability(character) {
    var lastAttack: Int = -1

    override fun available(): Boolean {
        return true
    }

    override fun cast() {
        TODO("Not yet implemented")
    }
}
