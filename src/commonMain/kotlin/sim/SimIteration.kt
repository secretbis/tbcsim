package sim

import character.*
import data.abilities.generic.MP5
import io.github.oshai.kotlinlogging.KotlinLogging
import sim.rotation.Rotation
import kotlin.js.JsExport
import kotlin.math.ceil
import character.classes.boss.Boss as BossClass
import character.races.Boss as BossRace

@JsExport
class SimIteration(
    _subject: Character,
    _rotation: Rotation,
    val opts: SimOptions,
    epStatMod: Stats? = null
) {
    val logger = KotlinLogging.logger {}

    // General sim state
    val serverTickMs = 2000
    var lastServerTickMs = 0

    val serverSlowTickMs = 3000
    var lastServerSlowTickMs = 0

    var lastMp5Tick = 0

    var tickNum: Int = 0
    var elapsedTimeMs: Int = 0

    var gcdBaseMs: Double = 1500.0
    val minGcdMs: Double = 1000.0

    val forceRecomputeThresholds = mutableMapOf(
        // 20% Execute/etc
        opts.durationMs * 0.20 to false,
        // 35% Dirty Deeds
        opts.durationMs * 0.35 to false
    )

    // Setup known participants
    val target: SimParticipant = defaultTarget()
    val subject: SimParticipant = SimParticipant(_subject, _rotation, this, epStatMod = epStatMod)

    // This is basically the non-sim target participants, and is the data needed for output
    // TODO: The rest of the party and raid
    val participants = listOfNotNull(subject, subject.pet)

    private val allParticipants: List<SimParticipant> = listOfNotNull(
        target,
    ) + participants

    // Initialize all non-target participants
    init {
        participants.forEach {
            it.init()

            // Cast any spells flagged in the rotation as precombat
            it.rotation.castAllRaidBuffs(it)
            it.rotation.castAllPrecombat(it)

            // Recompute after precombat casts
            it.recomputeStats()
        }
    }

    fun tick() {
        // Force a recompute at predefined thresholds, e.g. execute talents
        forceRecomputeThresholds.forEach {
            if(!it.value && elapsedTimeMs < it.key) {
                forceRecomputeThresholds[it.key] = true
                allParticipants.forEach { sp ->
                    sp.recomputeStats()
                }
            }
        }

        allParticipants.forEach {
            it.tick()
        }

        // Check debuffs
        allParticipants.forEach {
            it.debuffs.values.forEach { debuff ->
                if (debuff.shouldTick(it)) {
                    debuff.tick(it)
                }
            }
        }

        // MP5
        if (elapsedTimeMs - lastMp5Tick >= 5000) {
            allParticipants.forEach {
                if (it.resources.containsKey(Resource.Type.MANA)) {
                    it.addResource(it.stats.manaPer5Seconds, Resource.Type.MANA, MP5())
                }
            }
            lastMp5Tick = elapsedTimeMs
        }

        // Fire server tick procs
        if(elapsedTimeMs >= lastServerTickMs + serverTickMs) {
            lastServerTickMs = elapsedTimeMs
            allParticipants.forEach {
                it.fireProc(listOf(Proc.Trigger.SERVER_TICK), null, null, null)
                it.pet?.fireProc(listOf(Proc.Trigger.SERVER_TICK), null, null, null)
            }
        }

        if(elapsedTimeMs >= lastServerSlowTickMs + serverSlowTickMs) {
            lastServerSlowTickMs = elapsedTimeMs
            allParticipants.forEach {
                it.fireProc(listOf(Proc.Trigger.SERVER_SLOW_TICK), null, null, null)
            }
        }

        // Prune any buffs set to expire this tick
        allParticipants.forEach {
            it.pruneBuffs()
            it.pruneDebuffs()
        }
    }

    fun addRaidBuff(buff: Buff) {
        participants.forEach {
            it.addBuff(buff)
        }
    }

    fun addRaidDebuff(debuff: Debuff) {
        participants.forEach {
            it.addDebuff(debuff)
        }
    }

    fun getExpirationTick(buff: Buff): Int {
        return ceil((elapsedTimeMs + buff.durationMs) / opts.stepMs.toDouble()).toInt()
    }

    fun cleanup() {
        allParticipants.forEach {
            it.cleanup()
        }
    }

    private fun defaultTarget(): SimParticipant {
        val char = Character(
            BossClass(baseStats = Stats(
                armor = opts.targetArmor
            )),
            BossRace(),
            opts.targetLevel,
            subTypes = setOf(CharacterType.values()[opts.targetType])
        )

        return SimParticipant(char, Rotation(listOf(), false), this).init()
    }

    fun isExecutePhase(thresholdPercent: Double = 20.0): Boolean {
        // The last 20% of the duration is considered to be Execute phase
        val durLimit = (thresholdPercent / 100.0).coerceAtLeast(0.0)
        return elapsedTimeMs >= (1 - durLimit) * opts.durationMs
    }
}
