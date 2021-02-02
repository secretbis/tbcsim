package character.classes.warrior.talents

import character.*
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

class MaceSpec(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mace Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_GLANCE,
                Trigger.MELEE_BLOCK
            )
            override val type: Type = Type.PERCENT
            override val percentChance: Double = 5.0 * currentRank

            private fun isMace(item: Item): Boolean {
                return item.itemSubclass == Constants.ItemSubclass.MACE_2H ||
                       item.itemSubclass == Constants.ItemSubclass.MACE_1H
            }

            override fun shouldProc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
                return items?.all { isMace(it) } ?: false && super.shouldProc(sim, items, ability, event)
            }

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                val item = items?.get(0)
                if(item == null || !isMace(item)) {
                    logger.warn { "Tried to proc warrior Mace Specialization, but the context was not a mace." }
                    return
                }

                sim.addResource(7, Resource.Type.RAGE)
            }
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
