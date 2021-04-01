package character.classes.hunter.pet

import character.Ability
import character.classes.hunter.pet.abilities.LightningBreathRank6
import data.model.Item

class WindSerpent : HunterPet(petDamageMultiplier = 1.1) {
    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            "Lightning Breath" -> LightningBreathRank6()
            else -> super.abilityFromString(name, item)
        }
    }
}
