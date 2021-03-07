package character

import data.model.Item
import mu.KotlinLogging
import sim.Event
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
        // Melee non-hits
        MELEE_MISS,
        MELEE_DODGE,
        MELEE_PARRY,
        MELEE_BLOCK,
        MELEE_GLANCE,

        // Spells
        SPELL_HIT,
        SPELL_CRIT,
        SPELL_RESIST,

        // Damage types
        PHYSICAL_DAMAGE,
        HOLY_DAMAGE,
        FIRE_DAMAGE,
        NATURE_DAMAGE,
        FROST_DAMAGE,
        SHADOW_DAMAGE_PERIODIC,
        SHADOW_DAMAGE_NON_PERIODIC,
        ARCANE_DAMAGE,

        // Mechanics
        SERVER_TICK,

        // Specifics
        SHAMAN_CAST_SHOCK,
        SHAMAN_CAST_LIGHTNING_BOLT,
        SHAMAN_CAST_CHAIN_LIGHTNING,

        WARLOCK_CRIT_SHADOW_BOLT,
        WARLOCK_TICK_CORRUPTION
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
    open fun percentChance(sim: SimIteration): Double = 0.0

    // Many procs have ICDs
    open fun cooldownMs(sim: SimIteration): Int = 0

    open class State {
        var cooldownStartMs: Int = -1
    }

    internal open fun stateFactory(): State {
        return State()
    }

    internal fun state(sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        var state = sim.procState[this]
        if(state == null) {
            state = stateFactory()
            sim.procState[this] = state
        }
        return state
    }

    open fun afterProc(sim: SimIteration) {
        // Store individual cooldown state
        val state = state(sim)
        state.cooldownStartMs = sim.elapsedTimeMs
    }

    open fun shouldProc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
        val chances: MutableList<Double> = mutableListOf()

        // Return false if on ICD
        val state = state(sim)
        val offCooldown = state.cooldownStartMs == -1 || (state.cooldownStartMs + cooldownMs(sim) <= sim.elapsedTimeMs)
        if(!offCooldown) {
            return false
        }

        if(requiresItem) {
            if(items.isNullOrEmpty()) {
                logger.warn { "Attempted to proc an ItemProc, but got no items from ability: ${ability?.name}" }
            }

            // For each item specified, add a chance to proc
            for (item in items ?: listOf()) {
                chances.add(
                    when (type) {
                        // PPM always uses the BASE item speed, not hasted
                        Type.PPM -> (item.speed / 1000.0) * ppm / 60.0
                        Type.PERCENT -> percentChance(sim) / 100.0
                        Type.STATIC -> 100.0
                    }
                )
            }
        } else {
            chances.add(
                when (type) {
                    Type.PPM -> {
                        // Try to use the procced item if it is a weapon
                        val itemFromProc = items?.find { it === sim.subject.gear.mainHand  } ?: items?.find { it === sim.subject.gear.offHand }

                        if(itemFromProc == null) {
                            logger.debug { "Attempted to compute a PPM without an Item from ability: ${ability?.name}" }
                            0.0
                        } else {
                            (itemFromProc.speed / 1000.0) * ppm / 60.0
                        }
                    }
                    Type.PERCENT -> percentChance(sim) / 100.0
                    Type.STATIC -> 100.0
                }
            )
        }

        // RNG according to configuration
        val roll = Random.nextDouble()
        return chances.any { roll < it }
    }

    abstract fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?)
}
