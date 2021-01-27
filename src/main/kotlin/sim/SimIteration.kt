package sim

import character.*
import character.auto.MeleeMainHand
import character.auto.MeleeOffHand
import character.classes.boss.Boss as BossClass
import character.races.Boss as BossRace
import data.model.Item
import mu.KotlinLogging
import sim.rotation.Rotation

class SimIteration(
    val subject: Character,
    val rotation: Rotation,
    val opts: SimOptions
) {
    val logger = KotlinLogging.logger {}

    val target: Character = defaultTarget()

    val serverTickMs = 2000
    var tick: Int = 0
    var elapsedTimeMs: Int = 0
    var events: MutableList<Event> = mutableListOf()
    var autoAttack: List<Ability> = listOf()
    var buffs: MutableList<Buff> = mutableListOf()
    var debuffs: MutableList<Debuff> = mutableListOf()

    // Buffs need a place to store state per iteration
    // Store individual data per instance and store shared data per-string (generally the buff name)
    val buffState: MutableMap<Buff, Buff.State> = mutableMapOf()
    val sharedBuffState: MutableMap<String, Buff.State> = mutableMapOf()
    val abilityState: MutableMap<String, Ability.State> = mutableMapOf()
    val sharedAbilityState: MutableMap<String, Ability.State> = mutableMapOf()
    val debuffState: MutableMap<Buff, Buff.State> = mutableMapOf()
    val sharedDebuffState: MutableMap<String, Buff.State> = mutableMapOf()

    // Global state
    var gcdEndMs: Int = 0
    var castEndMs: Int = 0

    fun onGcd(): Boolean {
        return elapsedTimeMs < gcdEndMs
    }

    fun isCasting(): Boolean {
        return elapsedTimeMs < castEndMs
    }

    init {
        // Add auto-attack, if allowed
        if(subject.klass.allowAutoAttack) {
            autoAttack = listOf(
                MeleeMainHand(),
                MeleeOffHand()
            )
        }

        // Collect buffs from class, talents, gear, and etc
        subject.klass.buffs.forEach { addBuff(it) }
        subject.klass.talents.filter { it.value.currentRank > 0 }.forEach {
            it.value.buffs(this).forEach { buff -> addBuff(buff) }
        }
        subject.gear.buffs().forEach { addBuff(it) }

        // Compute initial stats
        recomputeStats()

        // Cast any spells flagged in the rotation as precombat
        rotation.precombat(this)
    }

    private fun recomputeStats() {
        subject.computeStats(this, buffs)
        target.computeStats(this, debuffs)
    }

    private fun pruneBuffs() {
        val buffsToRemove = buffs.filter {
            it.isFinished(this)
        }
        buffsToRemove.forEach {
            it.reset(this)
            logEvent(Event(
                eventType = Event.Type.BUFF_END,
                buff = it
            ))
        }
        buffs.removeAll(buffsToRemove)

        // Compute stats if something about our buffs changed
        if(buffsToRemove.isNotEmpty()) {
            recomputeStats()
        }
    }

    private fun pruneDebuffs() {
        val debuffsToRemove = debuffs.filter {
            it.isFinished(this)
        }
        debuffsToRemove.forEach {
            it.reset(this)
            logEvent(Event(
                eventType = Event.Type.DEBUFF_END,
                buff = it
            ))
        }
        debuffs.removeAll(debuffsToRemove)

        if(debuffsToRemove.isNotEmpty()) {
            recomputeStats()
        }
    }

    fun tick() {
        // Filter out and reset any expired buffs/debuffs
        pruneBuffs()
        pruneDebuffs()

        // Find and cast next rotation ability
        if(!isCasting() && !onGcd()) {
            val rotationAbility = rotation.next(this)

            if(rotationAbility != null) {
                rotationAbility.cast(this)
                rotationAbility.afterCast(this)

                // Log cast event
                // TODO: Is the SPELL_START event useful?
                logEvent(Event(
                    eventType = Event.Type.SPELL_CAST,
                    abilityName = rotationAbility.name
                ))

                // Set next cast times, and add latency if configured
                gcdEndMs = elapsedTimeMs + rotationAbility.gcdMs(this) + opts.latencyMs
                castEndMs = elapsedTimeMs + rotationAbility.castTimeMs(this) + opts.latencyMs
            }
        }

        // Do auto attacks
        autoAttack.forEach {
            if(it.available(this)) it.cast(this)
        }

        // Check debuffs
        debuffs.forEach {
            if(it.shouldTick(this)) {
                it.tick(this)
            }
        }

        // Fire server tick proc
        if(elapsedTimeMs % serverTickMs == 0) {
            fireProc(listOf(Proc.Trigger.SERVER_TICK))
        }
    }

     fun cleanup() {
         // Log end for all buffs
         buffs.forEach {
             logEvent(Event(
                eventType = Event.Type.BUFF_END,
                buff = it
            ))
         }

         debuffs.forEach {
             logEvent(Event(
                eventType = Event.Type.DEBUFF_END,
                buff = it
            ))
         }
     }

    fun addBuff(buff: Buff) {
        // Refresh, and flag buffs as changed
        buff.refresh(this)

        // If this is a new buff, add it
        val exists = buffs.find { it === buff } != null
        if(!exists) {
            // If this buff is mutex with others, remove any existing with that class
            if(buff.mutex != Buff.Mutex.NONE) {
                val toRemove = buffs.filter { it.mutex == buff.mutex }
                toRemove.forEach {
                    it.reset(this)
                    logEvent(Event(
                        eventType = Event.Type.BUFF_END,
                        buff = it
                    ))
                }
                buffs.removeAll(toRemove)
            }

            buffs.add(buff)
            logEvent(Event(
                eventType = Event.Type.BUFF_START,
                buff = buff
            ))

            // Always recompute after adding a buff
            recomputeStats()
        } else {
            logEvent(Event(
                eventType = Event.Type.BUFF_REFRESH,
                buff = buff
            ))
        }
    }

    fun addDebuff(debuff: Debuff) {
        debuff.refresh(this)

        // If this is a new debuff, add it
        val exists = debuffs.find { it === debuff } != null
        if(!exists) {
            debuffs.add(debuff)
            logEvent(Event(
                eventType = Event.Type.DEBUFF_START,
                buff = debuff
            ))

            // Always recompute after adding a debuff
            recomputeStats()
        } else {
            logEvent(Event(
                eventType = Event.Type.DEBUFF_REFRESH,
                buff = debuff
            ))
        }
    }

    fun fireProc(triggers: List<Proc.Trigger>, items: List<Item>? = null, ability: Ability? = null) {
        // Collect fireable procs
        val allProcs: MutableSet<Proc> = mutableSetOf()
        for(trigger in triggers) {
            // Get procs from active buffs
            buffs.forEach { buff ->
                buff.procs(this).filter { proc -> proc.triggers.contains(trigger) }.forEach {
                    allProcs.add(it)
                }
            }
        }

        // Fire all found procs
        allProcs.forEach {
            if(it.shouldProc(this, items, ability)) {
                it.proc(this, items, ability)

                // Always check buff/debuff state after any proc
                pruneBuffs()
                pruneDebuffs()
            }
        }
    }

    fun logEvent(event: Event) {
        // Auto-set tick and time if not specified
        if(event.tick == -1) {
            event.tick = tick
        }

        if(event.timeMs == -1) {
            event.timeMs = elapsedTimeMs
        }

        logger.debug { "Got event: ${event.abilityName} - ${event.tick} (${event.tick * opts.stepMs}ms) - ${event.eventType} - ${event.result} - ${event.amount}" }

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
