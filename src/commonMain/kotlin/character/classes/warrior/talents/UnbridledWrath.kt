package character.classes.warrior.talents

import character.*
import data.model.Item
import sim.Event
import sim.SimIteration

class UnbridledWrath(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Unbridled Wrath"
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

            override val type: Type = Type.PPM
            override val ppm: Double = 3.0 * currentRank

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                sim.addResource(1, Resource.Type.RAGE)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
