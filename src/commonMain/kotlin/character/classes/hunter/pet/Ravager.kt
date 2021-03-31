package character.classes.hunter.pet

import character.Ability
import data.model.Item
import sim.SimParticipant

class Ravager(owner: SimParticipant) : HunterPet(owner, petDamageMultiplier = 1.1) {
    override fun abilityFromString(name: String, item: Item?): Ability? {
        return super.abilityFromString(name, item)
    }
}
