package character.classes.hunter.pet

import character.Ability
import character.classes.hunter.pet.abilities.GoreRank9
import data.model.Item

class Ravager : HunterPet(petDamageMultiplier = 1.1) {
    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            "Gore" -> GoreRank9()
            else -> super.abilityFromString(name, item)
        }
    }
}
