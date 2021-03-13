package character

import sim.rotation.Rotation
import character.races.Pet as PetRace

class Pet(klass: Class, val rotation: Rotation) : Character(klass, PetRace()) {
    fun petTypesByName(name: String) {

    }
}
