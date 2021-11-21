package data.enchants

import character.*
import data.model.Enchant
import data.model.Item
import sim.Event
import sim.SimIteration
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class Crusader(item: Item) : Enchant(item) {
    override val id: Int = 20034
    override val name: String = "Crusader (static) ${item.uniqueName}"
    override val icon: String = "spell_holy_blessingofstrength.jpg"
    override var displayName: String? = "Crusader"
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
                throw IllegalArgumentException("Crusader can only be applied to weapons")
            }

            val buff = object : ItemBuff(listOf(item)) {
                override val name: String = "Crusader $suffix"
                override val icon: String = "spell_holy_blessingofstrength.jpg"
                override val durationMs: Int = 15000

                override fun modifyStats(sp: SimParticipant): Stats {
                    return Stats(strength = 100)
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
                    override val ppm: Double = 1.0
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
