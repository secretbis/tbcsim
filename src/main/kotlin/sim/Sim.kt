package sim

import character.Ability
import character.Character
import character.auto.MeleeMainHand
import character.auto.MeleeOffHand
import mu.KotlinLogging
import character.classes.boss.Boss as BossClass
import character.races.Boss as BossRace
import sim.rotation.Rotation
import sim.rotation.Rule

class Sim (
    val subject: Character,
    val rotation: Rotation,
    val opts: SimOptions
) {
    class Iteration {
        var tick: Int = 0
        var elapsedTimeMs: Int = 0
        var events: MutableList<Event> = mutableListOf()
    }

    val logger = KotlinLogging.logger {}
    val target = getDefaultTarget()
    val autoAttack: List<Ability>

    init {
        subject.sim = this
        target.sim = this

        // Add auto-attack, if allowed
        autoAttack =
            if(subject.klass.allowAutoAttack) {
                if(subject.isDualWielding()) {
                    listOf(
                        MeleeMainHand(this),
                        MeleeOffHand(this)
                    )
                } else {
                    listOf(
                        MeleeMainHand(this)
                    )
                }
            } else {
                listOf()
            }
    }

    val iterations = mutableListOf<Iteration>()
    lateinit var currentIteration: Iteration

    fun sim() {
        // Cast any spells flagged in the rotation as precombat
        rotation.rules.filter { it.type == Rule.Type.PRECOMBAT }.forEach {
            it.ability.cast()
        }

        // Start combat
        for(iter in 1..opts.iterations) {
            currentIteration = Iteration()
            for(timeMs in 0..opts.durationMs step opts.stepMs) {
                currentIteration.tick++
                currentIteration.elapsedTimeMs = timeMs
                step(timeMs)
            }
            iterations.add(currentIteration)
        }
    }

    private fun step(timeMs: Int) {
        // TODO: Clear buffs that have expired, recompute if needed
        // TODO: Find and cast next rotation ability
        autoAttack.forEach {
            if(it.available()) it.cast()
        }
    }

    private fun setupAutoAttack(timeMs: Int) {

    }

    fun logEvent(event: Event) {
        // Auto-set tick if not specified
        if(event.tick == -1) {
            event.tick = currentIteration.tick
        }

        logger.debug { "Got event: ${event.tick} (${event.tick * opts.stepMs}ms) - ${event.type} - ${event.result} - ${event.amount}" }

        currentIteration.events.add(event)
    }

    private fun getDefaultTarget(): Character {
        return Character(
            BossClass(),
            BossRace(),
            opts.targetLevel
        )
    }
}
