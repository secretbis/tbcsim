package data.enchants

import character.*
import data.model.Item
import sim.Event
import sim.SimIteration

// Fun blog about Goose and Executioner:
// https://warcraft.blizzplanet.com/blog/comments/world_of_warcraft_burning_crusade___enchanting___executioner_vs_mongoose
class Mongoose(val item: Item) : ItemBuff(listOf(item)) {
    override val name: String = "Mongoose (enchant)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    private var _procs: List<Proc>? = null
    private fun makeProcs(sim: SimIteration): List<Proc> {
        if(_procs == null) {
            val suffix = if (item === sim.subject.gear.mainHand) {
                "(MH)"
            } else if (item === sim.subject.gear.offHand) {
                "(OH)"
            } else {
                throw IllegalArgumentException("Mongoose can only be applied to weapons")
            }

            val buff = object : ItemBuff(listOf(item)) {
                override val name: String = "Mongoose $suffix"
                override val durationMs: Int = 15000

                override fun modifyStats(sim: SimIteration): Stats {
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
                        Trigger.MELEE_WHITE_CRIT
                    )

                    override val type: Type = Type.PPM
                    override val ppm: Double = 1.2
                    override val requiresItem: Boolean = true

                    override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                        sim.addBuff(buff)
                    }
                }
            )
        }

        return _procs!!
    }

    override fun procs(sim: SimIteration): List<Proc> = makeProcs(sim)
}
