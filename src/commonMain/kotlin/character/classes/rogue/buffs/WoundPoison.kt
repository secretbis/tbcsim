package character.classes.rogue.buffs

import character.*
import character.classes.rogue.abilities.*
import data.Constants
import data.model.Item
import data.model.TempEnchant
import sim.Event
import sim.SimParticipant
import character.classes.rogue.talents.*

class WoundPoison(sourceItem: Item) : Poison(sourceItem) {

    override val name = "Wound Poison ${sourceItem.uniqueName}"
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val durationMs: Int = 60 * 60 * 1000
    override val hidden: Boolean = true

    val proc = object : ItemProc(sourceItems) {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT
        )

        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double {
            val improved = sp.character.klass.talents[ImprovedPoisons.name] as ImprovedPoisons?
            val increasedPercent = improved?.poisonApplyChancePercent() ?: 0.0
            return 30.0 + increasedPercent
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            if(poisonAbility == null) {
                val suffix = when(sourceItem) {
                    sp.character.gear.mainHand -> "(MH)"
                    sp.character.gear.offHand -> "(OH)"
                    else -> "(Unknown)"
                }
                val name = "Wound Poison $suffix"
                poisonAbility = WoundPoison(name, sourceItem)
            }

            if(poisonAbility!!.available(sp)) {
                poisonAbility!!.cast(sp)

                sp.logEvent(
                    Event(
                        eventType = Event.Type.PROC,
                        abilityName = poisonAbility!!.name
                    )
                )
            }
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
