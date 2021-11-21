package data.abilities.generic

import character.Ability

class MP5 : Ability() {
    companion object {
        const val name = "MP5"
    }

    override val name: String = Companion.name
    override val icon: String = "spell_magic_managain.jpg"
}
