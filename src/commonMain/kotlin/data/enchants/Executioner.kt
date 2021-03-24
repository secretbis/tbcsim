package data.enchants

import character.*
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.Event
import sim.SimIteration
import sim.SimParticipant
import kotlin.js.JsExport

// Fun blog about Goose and Executioner:
// https://warcraft.blizzplanet.com/blog/comments/world_of_warcraft_burning_crusade___enchanting___executioner_vs_mongoose
@JsExport
class Executioner(item: Item) : Enchant(item) {
    // Executioner always stacks once even if enchanted and procced twice, so use a singleton Buff object to track it
    companion object {
        private var singletonBuff: Buff? = null

        fun singletonBuff(sourceItems: List<Item>): Buff {
            if(singletonBuff == null) {
                singletonBuff = object : ItemBuff(sourceItems) {
                    override val name: String = "Executioner"
                    override val durationMs: Int = 15000

                    override fun modifyStats(sp: SimParticipant): Stats {
                        return Stats(armorPen = 840)
                    }
                }
            }

            return singletonBuff!!
        }
    }

    override val id: Int = 42976
    override val name: String = "Executioner (enchant)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal

    private var _procs: List<Proc>? = null
    private fun makeProcs(sp: SimParticipant): List<Proc> {
        if(_procs == null) {
            // Find items
            val sourceItems = listOfNotNull(
                if (sp.character.gear.mainHand.enchant is Executioner) {
                    sp.character.gear.mainHand
                } else {
                    null
                },
                if (sp.character.gear.offHand.enchant is Executioner) {
                    sp.character.gear.offHand
                } else {
                    null
                }
            )

            _procs = listOf(
                object : Proc() {
                    override val triggers: List<Trigger> = listOf(
                        Trigger.MELEE_AUTO_HIT,
                        Trigger.MELEE_AUTO_CRIT,
                        Trigger.MELEE_YELLOW_HIT,
                        Trigger.MELEE_YELLOW_CRIT,
                        Trigger.MELEE_WHITE_HIT,
                        Trigger.MELEE_WHITE_CRIT,
                        Trigger.MELEE_BLOCK,
                        Trigger.MELEE_GLANCE
                    )

                    override val type: Type = Type.PPM
                    override val ppm: Double = 1.2
                    override val requiresItem: Boolean = true

                    override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                        sp.addBuff(singletonBuff(sourceItems))
                    }
                }
            )
        }

        return _procs!!
    }

    override fun procs(sp: SimParticipant): List<Proc> = makeProcs(sp)
}
