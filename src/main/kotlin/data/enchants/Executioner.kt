package data.enchants

import character.*
import data.model.Item
import sim.SimIteration

// Fun blog about Goose and Executioner:
// https://warcraft.blizzplanet.com/blog/comments/world_of_warcraft_burning_crusade___enchanting___executioner_vs_mongoose
class Executioner : Buff() {
    // Executioner always stacks once even if enchanted and procced twice, so use a singleton Buff object to track it
    companion object {
        private var singletonBuff: Buff? = null

        fun singletonBuff(sourceItems: List<Item>): Buff {
            if(singletonBuff == null) {
                singletonBuff = object : ItemBuff(sourceItems) {
                    override val name: String = "Executioner"
                    override val durationMs: Int = 15000

                    override fun modifyStats(sim: SimIteration): Stats {
                        return Stats(armorPen = 840)
                    }
                }
            }

            return singletonBuff!!
        }
    }

    override val name: String = "Executioner (enchant)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    private var _procs: List<Proc>? = null
    private fun makeProcs(sim: SimIteration): List<Proc> {
        if(_procs == null) {
            // Find items
            val sourceItems = listOfNotNull(
                if (sim.subject.gear.mainHand.enchant is Executioner) {
                    sim.subject.gear.mainHand
                } else {
                    null
                },
                if (sim.subject.gear.offHand.enchant is Executioner) {
                    sim.subject.gear.offHand
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
                        Trigger.MELEE_WHITE_CRIT
                    )

                    override val type: Type = Type.PPM
                    override val ppm: Double = 1.2
                    override val requiresItem: Boolean = true

                    override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                        sim.addBuff(singletonBuff(sourceItems))
                    }
                }
            )
        }

        return _procs!!
    }

    override fun procs(sim: SimIteration): List<Proc> = makeProcs(sim)
}
