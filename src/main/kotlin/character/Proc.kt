package character

import data.model.Item
import sim.SimIteration

abstract class Proc {
    enum class Trigger {
        // Specifically auto-attacks
        MELEE_AUTO_HIT,
        MELEE_AUTO_CRIT,
        // An auto-attack or bonus attack that deals white damage
        MELEE_WHITE_HIT,
        MELEE_WHITE_CRIT,
        // A melee special attack
        MELEE_YELLOW_HIT,
        MELEE_YELLOW_CRIT,
        // Spells
        SPELL_HIT,
        SPELL_CRIT
    }

    open val triggers: List<Trigger> = listOf()
    open val ppm: Double = 0.0

    abstract fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?)
}
