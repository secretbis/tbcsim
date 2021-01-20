package character

import data.model.Item
import sim.Sim

abstract class Proc {
    enum class Trigger {
        MELEE_WHITE_HIT,
        MELEE_WHITE_CRIT,
        MELEE_YELLOW_HIT,
        MELEE_YELLOW_CRIT,
        SPELL_HIT,
        SPELL_CRIT
    }

    open val triggers: List<Trigger> = listOf()
    open val ppm: Double = 0.0

    abstract fun proc(sim: Sim, items: List<Item>?, ability: Ability?)
}
