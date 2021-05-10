package character.classes.shaman.buffs

import character.classes.shaman.abilities.FlametongueWeapon as FlametongueWeaponAbility
import character.Ability
import character.ItemProc
import character.Proc
import data.Constants
import data.model.Item
import data.model.TempEnchant
import sim.Event
import sim.EventType
import sim.SimParticipant

class FlametongueWeapon(sourceItem: Item) : TempEnchant(sourceItem) {
    companion object {
        const val name = "Flametongue Weapon"
    }
    override val id: Int = 25489
    override val name = "Flametongue Weapon (static) ${sourceItem.uniqueName}"
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val durationMs: Int = 30 * 60 * 1000

    val proc = object : ItemProc(sourceItems) {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT
        )

        override val type: Type = Type.STATIC

        var ftAbility: Ability? = null

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            if (ftAbility == null) {
                val suffix = when (sourceItem) {
                    sp.character.gear.mainHand -> "(MH)"
                    sp.character.gear.offHand -> "(OH)"
                    else -> "(Unknown)"
                }
                val name = "Flametongue Weapon $suffix"
                ftAbility = FlametongueWeaponAbility(name, sourceItem)
            }

            if (ftAbility!!.available(sp)) {
                ftAbility!!.cast(sp)

                sp.logEvent(
                    Event(
                        eventType = EventType.PROC,
                        abilityName = ftAbility!!.name
                    )
                )
            }
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
