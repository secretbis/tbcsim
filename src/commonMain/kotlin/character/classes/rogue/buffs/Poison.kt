package character.classes.rogue.buffs

import character.*
import character.classes.rogue.abilities.*
import data.Constants
import data.model.Item
import data.model.TempEnchant
import sim.Event
import sim.SimParticipant
import character.classes.rogue.talents.*

abstract class Poison(sourceItem: Item) : TempEnchant(sourceItem) {

    override fun modifyStats(sp: SimParticipant): Stats? {
        sourceItems.first().tempEnchant = this
        return null
    }

    public var poisonAbility: Ability? = null
}
