package character

import character.classes.hunter.pet.Cat
import character.classes.hunter.pet.Ravager
import character.classes.hunter.pet.WindSerpent
import character.classes.mage.pet.WaterElemental
import character.classes.priest.pet.Shadowfiend
import io.github.oshai.kotlinlogging.KotlinLogging
import character.races.Pet as PetRace

class Pet(klass: Class, val startsActive: Boolean = true) : Character(klass, PetRace()) {
    companion object {
        val logger = KotlinLogging.logger {}

        fun petClassByName(name: String) : Class {
            return when(name) {
                "Cat" -> Cat()
                "Ravager" -> Ravager()
                "Shadowfiend" -> Shadowfiend()
                "Water Elemental" -> WaterElemental()
                "Wind Serpent" -> WindSerpent()
                else -> {
                    logger.warn { "Invalid pet type $name - defaulting to kitty cat" }
                    Cat()
                }
            }
        }
    }

    constructor(name: String, startsActive: Boolean = true) : this(petClassByName(name), startsActive)
}
