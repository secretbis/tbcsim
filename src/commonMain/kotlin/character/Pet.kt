package character

import character.classes.hunter.pet.Cat
import character.classes.hunter.pet.HunterPet
import character.classes.hunter.pet.Ravager
import character.classes.hunter.pet.WindSerpent
import mu.KotlinLogging
import character.races.Pet as PetRace

class Pet(klass: Class) : Character(klass, PetRace()) {
    companion object {
        val logger = KotlinLogging.logger {}

        fun petClassByName(name: String) : HunterPet {
            return when(name) {
                "Cat" -> Cat()
                "Ravager" -> Ravager()
                "Wind Serpent" -> WindSerpent()
                else -> {
                    logger.warn { "Invalid pet type $name - defaulting to kitty cat" }
                    Cat()
                }
            }
        }
    }

    constructor(name: String) : this(petClassByName(name))
}
