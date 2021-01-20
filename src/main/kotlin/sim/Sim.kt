package sim

import character.Ability
import character.Buff
import character.Character
import character.Proc
import character.auto.MeleeMainHand
import character.auto.MeleeOffHand
import data.model.Item
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
        var procs: MutableList<Proc> = mutableListOf()
        var buffs: MutableList<Buff> = mutableListOf()
    }

    val logger = KotlinLogging.logger {}
    val target = getDefaultTarget()

    val iterations = mutableListOf<Iteration>()
    lateinit var currentIteration: Iteration

    fun sim() {
        // Start combat
        for(iter in 1..opts.iterations) {
            currentIteration = Iteration()

            // Setup
            setupIteration()

            // Simulate
            for(timeMs in 0..opts.durationMs step opts.stepMs) {
                currentIteration.tick++
                currentIteration.elapsedTimeMs = timeMs
                step(timeMs)
            }

            iterations.add(currentIteration)
        }

        // Stats
        Stats.dps(this)
        Stats.resultsByAbility(this)
    }

    fun setupIteration() {
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

        // Collect procs from class, talents, gear, and etc.
        currentIteration.procs.addAll(subject.klass.procs)
        subject.klass.talents.filter { it.currentRank > 0 }.forEach {
            currentIteration.procs.addAll(it.procs)
        }
        currentIteration.procs.addAll(subject.gear.procs())

        // Collect buffs from class, talents, gear, and etc
        currentIteration.buffs.addAll(subject.klass.buffs)
        subject.klass.talents.filter { it.currentRank > 0 }.forEach {
            currentIteration.buffs.addAll(it.buffs)
        }
        currentIteration.buffs.addAll(subject.gear.buffs())
    }

    private fun step(timeMs: Int) {
        // Filter out and reset any expired buffs
        val toRemove = currentIteration.buffs.filter {
            currentIteration.elapsedTimeMs > it.appliedAtMs + it.durationMs
        }
        toRemove.forEach { it.reset() }
        currentIteration.buffs.removeAll(toRemove)

        // Compute stats
        subject.computeStats(this, currentIteration.buffs)

        // TODO: Find and cast next rotation ability

        // Do auto attacks
        currentIteration.autoAttack.forEach {
            if(it.available()) it.cast()
        }
    }

    fun addBuff(buff: Buff) {
        val exists = currentIteration.buffs.find { it === buff } != null
        buff.appliedAtMs = currentIteration.elapsedTimeMs

        // Handle buff refreshing, stacking, and etc.
        if(exists) {
            if (buff.maxStacks == 0) {
                // Refreshed above by resetting applied time
            } else {
                // Increase stacks
                // If max stacks, refresh
            }
        } else {
            // Not yet present
            currentIteration.buffs.add(buff)
        }
    }

    fun fireProc(triggers: List<Proc.Trigger>, items: List<Item>? = null, ability: Ability? = null) {
        for(trigger in triggers) {
            // Get any procs from the triggering item
            val allProcs: MutableList<Proc> = mutableListOf()
            if (items != null) {
                allProcs.addAll(items.flatMap { it.procs }.filter { it.triggers.contains(trigger) })
            }

            // Get procs from active buffs
            currentIteration.buffs.forEach { buff ->
                buff.procs.filter { proc -> proc.triggers.contains(trigger) }.forEach {
                    allProcs.add(it)
                }
            }

            // Get procs from static sources
            allProcs.addAll(currentIteration.procs)

            // Fire all procs
            allProcs.forEach {
                it.proc(this, items, ability)
            }
        }
    }

    fun logEvent(event: Event) {
        // Auto-set tick if not specified
        if(event.tick == -1) {
            event.tick = currentIteration.tick
        }

        logger.debug { "Got event: ${event.ability.name} - ${event.tick} (${event.tick * opts.stepMs}ms) - ${event.eventType} - ${event.result} - ${event.amount}" }

        currentIteration.events.add(event)
    }

    private fun getDefaultTarget(): Character {
        val char = Character(
            BossClass(),
            BossRace(),
            opts.targetLevel
        )

        char.computeStats(this, listOf())
        char.stats.armor = opts.targetArmor

        return char
    }
}
