package character

import data.model.Item
import mu.KotlinLogging
import sim.Event
import sim.SimParticipant
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

        // Special case a white hit that was replaced by a yellow hit
        // Windfury totem, for example, can proc off Heroic Strike/Cleave
        MELEE_REPLACED_AUTO_ATTACK_HIT,
        MELEE_REPLACED_AUTO_ATTACK_CRIT,

        // Ranged
        RANGED_AUTO_HIT,
        RANGED_AUTO_CRIT,
        RANGED_WHITE_HIT,
        RANGED_WHITE_CRIT,
        RANGED_YELLOW_HIT,
        RANGED_YELLOW_CRIT,
        RANGED_MISS,
        RANGED_BLOCK,

        // Pets
        PET_MELEE_HIT,
        PET_MELEE_CRIT,
        PET_SPELL_HIT,
        PET_SPELL_CRIT,

        // Spells
        SPELL_HIT,
        SPELL_CRIT,
        SPELL_RESIST,
        SPELL_CAST,

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
        SERVER_TICK, // 2s
        SERVER_SLOW_TICK,  // 3s

        // Specifics
        HUNTER_CAST_KILL_COMMAND,

        PRIEST_TICK_SHADOW_WORD_PAIN,

        ROGUE_CAST_CHEAP_SHOT,
        ROGUE_CAST_KIDNEY_SHOT,
        ROGUE_CAST_EVISCERATE,
        ROGUE_CAST_ENVENOM,
        ROGUE_CAST_RUPTURE,
        ROGUE_CAST_SLICE_AND_DICE,

        SHAMAN_CAST_SHOCK,
        SHAMAN_CAST_LIGHTNING_BOLT,
        SHAMAN_CAST_CHAIN_LIGHTNING,
        SHAMAN_CAST_STORMSTRIKE,
        SHAMAN_CRIT_LIGHTNING_BOLT,

        WARLOCK_CRIT_INCINERATE,
        WARLOCK_CRIT_SHADOW_BOLT,
        WARLOCK_HIT_INCINERATE,
        WARLOCK_HIT_SHADOW_BOLT,
        WARLOCK_TICK_CORRUPTION,

        WARRIOR_CAST_OVERPOWER
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
    open fun percentChance(sp: SimParticipant): Double = 0.0

    // Many procs have ICDs
    open fun cooldownMs(sp: SimParticipant): Int = 0

    open class State {
        var cooldownStartMs: Int = -1
    }

    internal open fun stateFactory(): State {
        return State()
    }

    internal fun state(sp: SimParticipant): State {
        // Create state object if it does not exist, and return it
        var state = sp.procState[this]
        if(state == null) {
            state = stateFactory()
            sp.procState[this] = state
        }
        return state
    }

    open fun afterProc(sp: SimParticipant) {
        // Store individual cooldown state
        val state = state(sp)
        state.cooldownStartMs = sp.sim.elapsedTimeMs
    }

    open val identifier: Long = Random.nextLong()

    open fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
        val chances: MutableList<Double> = mutableListOf()

        // Return false if on ICD
        val state = state(sp)
        val offCooldown = state.cooldownStartMs == -1 || (state.cooldownStartMs + cooldownMs(sp) <= sp.sim.elapsedTimeMs)
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
                        Type.PERCENT -> percentChance(sp) / 100.0
                        Type.STATIC -> 100.0
                    }
                )
            }
        } else {
            chances.add(
                when (type) {
                    Type.PPM -> {
                        // Try to use the procced item if it is a weapon
                        val itemFromProc = items?.find { it === sp.character.gear.mainHand  } ?: items?.find { it === sp.character.gear.offHand }

                        if(itemFromProc == null) {
                            logger.debug { "Attempted to compute a PPM without an Item from ability: ${ability?.name}" }
                            0.0
                        } else {
                            (itemFromProc.speed / 1000.0) * ppm / 60.0
                        }
                    }
                    Type.PERCENT -> percentChance(sp) / 100.0
                    Type.STATIC -> 100.0
                }
            )
        }

        // RNG according to configuration
        val roll = Random.nextDouble()
        return chances.any { roll < it }
    }

    abstract fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?)
}
