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
        var autoAttack: List<Ability> = listOf()
    }

    val logger = KotlinLogging.logger {}
    val target = getDefaultTarget()


    init {
        subject.sim = this
        target.sim = this
    }

    val iterations = mutableListOf<Iteration>()
    lateinit var currentIteration: Iteration

    fun sim() {
        // Start combat
        for(iter in 1..opts.iterations) {
            currentIteration = Iteration()

            // Cast any spells flagged in the rotation as precombat
            rotation.rules.filter { it.type == Rule.Type.PRECOMBAT }.forEach {
                it.ability.cast()
            }

            // Add auto-attack, if allowed
            if(subject.klass.allowAutoAttack) {
                currentIteration.autoAttack = listOf(
                    MeleeMainHand(this),
                    MeleeOffHand(this)
                )
            }

            for(timeMs in 0..opts.durationMs step opts.stepMs) {
                currentIteration.tick++
                currentIteration.elapsedTimeMs = timeMs
                step(timeMs)
            }

            iterations.add(currentIteration)
        }

        // Stats
        Stats.dps(this)
    }

    private fun step(timeMs: Int) {
        // TODO: Clear buffs that have expired, recompute if needed
        // TODO: Find and cast next rotation ability
        currentIteration.autoAttack.forEach {
            if(it.available()) it.cast()
        }
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
