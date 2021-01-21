package sim

import character.Ability
import character.Buff
import character.Character
import character.Proc
import character.auto.MeleeMainHand
import character.auto.MeleeOffHand
import character.classes.boss.Boss as BossClass
import character.races.Boss as BossRace
import data.model.Item
import mu.KotlinLogging
import sim.rotation.Rotation
import sim.rotation.Rule

class SimIteration(
    val subject: Character,
    val rotation: Rotation,
    val opts: SimOptions
) {
    val logger = KotlinLogging.logger {}

    val target: Character = defaultTarget()

    var tick: Int = 0
    var elapsedTimeMs: Int = 0
    var events: MutableList<Event> = mutableListOf()
    var autoAttack: List<Ability> = listOf()
    var procs: MutableList<Proc> = mutableListOf()
    var buffs: MutableList<Buff> = mutableListOf()

    // Buffs need a place to store state per iteration
    val buffState: MutableMap<Buff, Buff.State> = mutableMapOf()

    // Global state
    var gcdEndMs: Int = 0
    var castEndMs: Int = 0

    // Flag to track if we need to recompute stats on the next tick
    var recomputeStatsOnNextTick: Boolean = false

    fun onGcd(): Boolean {
        return elapsedTimeMs < gcdEndMs
    }

    fun isCasting(): Boolean {
        return elapsedTimeMs < castEndMs
    }

    init {
        // Cast any spells flagged in the rotation as precombat
        rotation.rules.filter { it.type == Rule.Type.PRECOMBAT }.forEach {
            it.ability.cast()
        }

        // Add auto-attack, if allowed
        if(subject.klass.allowAutoAttack) {
            autoAttack = listOf(
                MeleeMainHand(this),
                MeleeOffHand(this)
            )
        }

        // Collect procs from class, talents, gear, and etc.
        procs.addAll(subject.klass.procs)
        subject.talents.filter { it.value.currentRank > 0 }.forEach {
            procs.addAll(it.value.procs)
        }
        procs.addAll(subject.gear.procs())

        // Collect buffs from class, talents, gear, and etc
        buffs.addAll(subject.klass.buffs)
        subject.talents.filter { it.value.currentRank > 0 }.forEach {
            buffs.addAll(it.value.buffs)
        }
        buffs.addAll(subject.gear.buffs())

        // Compute initial stats
        subject.computeStats(this, buffs)
    }

    fun tick() {
        // Filter out and reset any expired buffs
        val toRemove = buffs.filter {
            it.isFinished(this)
        }
        toRemove.forEach { it.reset(this) }
        buffs.removeAll(toRemove)

        // Compute stats if something about our buffs changed
        if(toRemove.isNotEmpty() || recomputeStatsOnNextTick) {
            subject.computeStats(this, buffs)
            recomputeStatsOnNextTick = false
        }

        // Find and cast next rotation ability
        if(!isCasting() && !onGcd()) {
            val rotationAbility = rotation.next(this)

            if(rotationAbility != null) {
                rotationAbility.cast()

                // Set next cast times, and add latency if configured
                gcdEndMs = elapsedTimeMs + rotationAbility.gcdMs() + opts.latencyMs
                castEndMs = elapsedTimeMs + rotationAbility.castTimeMs() + opts.latencyMs
            }
        }

        // Do auto attacks
        autoAttack.forEach {
            if(it.available()) it.cast()
        }
    }

    fun addBuff(buff: Buff) {
        // Refresh, and flag buffs as changed
        buff.refresh(this)
        recomputeStatsOnNextTick = true

        // If this is a new buff, add it
        val exists = buffs.find { it === buff } != null
        if(!exists) {
            buffs.add(buff)
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
            buffs.forEach { buff ->
                buff.procs.filter { proc -> proc.triggers.contains(trigger) }.forEach {
                    allProcs.add(it)
                }
            }

            // Get procs from static sources
            allProcs.addAll(procs.filter { proc -> proc.triggers.contains(trigger) })

            // Fire all found procs
            allProcs.forEach {
                it.proc(this, items, ability)
            }
        }
    }

    fun logEvent(event: Event) {
        // Auto-set tick if not specified
        if(event.tick == -1) {
            event.tick = tick
        }

        logger.debug { "Got event: ${event.ability.name} - ${event.tick} (${event.tick * opts.stepMs}ms) - ${event.eventType} - ${event.result} - ${event.amount}" }

        events.add(event)
    }

    private fun defaultTarget(): Character {
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
