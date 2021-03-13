package character.classes.pet

import character.Ability
import character.Character
import data.model.Item

class Cat(owner: Character) : Pet(owner) {
    override val petDamageMultiplier: Double = 1.1

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return super.abilityFromString(name, item)
    }
}
