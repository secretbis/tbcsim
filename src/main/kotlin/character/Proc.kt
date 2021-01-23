package character

import data.model.Item
import mu.KotlinLogging
import sim.SimIteration
import kotlin.random.Random

abstract class Proc {
    val logger = KotlinLogging.logger {}

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
        SPELL_CRIT,

        // Damage types
        PHYSICAL_DAMAGE,
        HOLY_DAMAGE,
        FIRE_DAMAGE,
        NATURE_DAMAGE,
        FROST_DAMAGE,
        SHADOW_DAMAGE,
        ARCANE_DAMAGE
    }

    enum class Type {
        // Triggers on a percentage chance per opportunity
        PERCENT,
        // Triggers based on an item and a PPM-based percentage per opportunity
        PPM,
        // Triggers on every opportunity
        STATIC
    }

    abstract val triggers: List<Trigger>
    open val requiresItem: Boolean = false

    abstract val type: Type
    open val ppm: Double = 0.0
    open val percentChance: Double = 0.0

    open fun shouldProc(sim: SimIteration, items: List<Item>?, ability: Ability?): Boolean {
        val chances: MutableList<Double> = mutableListOf()

        if(requiresItem) {
            if(items.isNullOrEmpty()) {
                logger.warn("Attempted to proc an ItemProc, but got no items from ability: ${ability?.name}")
            }

            // For each item specified, add a chance to proc
            for (item in items ?: listOf()) {
                chances.add(
                    when (type) {
                        // PPM always uses the BASE item speed, not hasted
                        Type.PPM -> (item.speed / 1000.0) * ppm / 60.0
                        Type.PERCENT -> percentChance / 100.0
                        Type.STATIC -> 100.0
                    }
                )
            }
        } else {
            chances.add(
                when (type) {
                    // Non-item procs cannot have PPM
                    Type.PPM -> {
                        logger.warn("Attempted to compute a PPM without an Item from ability: ${ability?.name}")
                        0.0
                    }
                    Type.PERCENT -> percentChance / 100.0
                    Type.STATIC -> 100.0
                }
            )
        }

        // RNG according to configuration
        val roll = Random.nextDouble()
        return chances.any { roll < it }
    }

    abstract fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?)
}
