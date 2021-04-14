package data.tempenchants

import character.Stats
import data.Constants
import data.model.Item
import data.model.TempEnchant
import sim.SimParticipant

class SuperiorWizardOil(item: Item) : TempEnchant(item) {
    override val name: String = "Superior Wizard Oil"
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val durationMs: Int = 60 * 60 * 1000

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(spellDamage = 42)
    }
}
