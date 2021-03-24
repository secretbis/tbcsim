package data.enchants

import character.*
import data.model.Enchant
import data.model.Item
import sim.Event
import sim.SimIteration
import sim.SimParticipant
import kotlin.js.JsExport

// Fun blog about Goose and Executioner:
// https://warcraft.blizzplanet.com/blog/comments/world_of_warcraft_burning_crusade___enchanting___executioner_vs_mongoose
@JsExport
class Mongoose(item: Item) : Enchant(item) {
    override val id: Int = 46536
    override val name: String = "Mongoose (static) ${item.uniqueName}"
    override var displayName: String? = "Mongoose"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val inventorySlot: Int = 13

    private var _procs: List<Proc>? = null
    private fun makeProcs(sp: SimParticipant): List<Proc> {
        if(_procs == null) {
            val suffix = if (item === sp.character.gear.mainHand) {
                "(MH)"
            } else if (item === sp.character.gear.offHand) {
                "(OH)"
            } else {
                throw IllegalArgumentException("Mongoose can only be applied to weapons")
            }

            val buff = object : ItemBuff(listOf(item)) {
                override val name: String = "Mongoose $suffix"
                override val durationMs: Int = 15000

                override fun modifyStats(sp: SimParticipant): Stats {
                    return Stats(physicalHasteRating = 30.0, agility = 120)
                }
            }

            _procs = listOf(
                object : ItemProc(listOf(item)) {
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
                        sp.addBuff(buff)
                    }
                }
            )
        }

        return _procs!!
    }

    override fun procs(sp: SimParticipant): List<Proc> = makeProcs(sp)
}
