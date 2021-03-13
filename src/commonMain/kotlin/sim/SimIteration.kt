package sim

import character.*
import mu.KotlinLogging
import sim.rotation.Rotation
import kotlin.js.JsExport
import kotlin.math.ceil
import character.classes.boss.Boss as BossClass
import character.races.Boss as BossRace

@JsExport
class SimIteration(
    _subject: Character,
    _rotation: Rotation,
    val opts: SimOptions
) {
    val logger = KotlinLogging.logger {}

    // Setup known participants
    val target: SimParticipant = defaultTarget()
    val subject: SimParticipant = SimParticipant(_subject, _rotation, this)
    val subjectPet: SimParticipant? = if(subject.character.pet != null) {
        SimParticipant(subject.character.pet, subject.character.pet.rotation, this)
    } else null

    // Universal sim state
    val serverTickMs = 2000
    var lastServerTickMs = 0

    val serverSlowTickMs = 3000
    var lastServerSlowTickMs = 0

    var lastMp5Tick = 0

    var tickNum: Int = 0
    var elapsedTimeMs: Int = 0

    var gcdBaseMs: Double = 1500.0
    val minGcdMs: Double = 1000.0

    val participants: List<SimParticipant>

    init {
        participants = listOfNotNull(
            target,
            subject,
            subjectPet
        )

        // TODO: The rest of the party and raid
    }

    fun tick() {
        participants.forEach {
            it.tick()
        }

        // Check debuffs
        participants.forEach {
            it.debuffs.values.forEach { debuff ->
                if (debuff.shouldTick(it)) {
                    debuff.tick(it)
                }
            }
        }

        // MP5
        participants.forEach {
            if (it.character.klass.resourceType == Resource.Type.MANA) {
                if (elapsedTimeMs - lastMp5Tick >= 5000) {
                    it.addResource(it.stats.manaPer5Seconds, Resource.Type.MANA)
                    lastMp5Tick = elapsedTimeMs
                }
            }
        }

        // Fire server tick procs
        if(elapsedTimeMs >= lastServerTickMs + serverTickMs) {
            lastServerTickMs = elapsedTimeMs
            participants.forEach {
                it.fireProc(listOf(Proc.Trigger.SERVER_TICK), null, null, null)
            }
        }

        if(elapsedTimeMs >= lastServerSlowTickMs + serverSlowTickMs) {
            lastServerSlowTickMs = elapsedTimeMs
            participants.forEach {
                it.fireProc(listOf(Proc.Trigger.SERVER_SLOW_TICK), null, null, null)
            }
        }

        // Prune any buffs set to expire this tick
        participants.forEach {
            it.pruneBuffs()
            it.pruneDebuffs()
        }
    }

    fun getExpirationTick(buff: Buff): Int {
        return ceil((elapsedTimeMs + buff.durationMs) / opts.stepMs.toDouble()).toInt()
    }

    fun cleanup() {
        participants.forEach {
            it.cleanup()
        }
    }

    private fun defaultTarget(): SimParticipant {
        val char = Character(
            BossClass(baseStats = Stats(
                armor = opts.targetArmor
            )),
            BossRace(),
            opts.targetLevel
        )

        return SimParticipant(char, Rotation(listOf(), false), this)
    }

    fun isExecutePhase(): Boolean {
        // The last 20% of the duration is considered to be Execute phase
        return elapsedTimeMs >= 0.8 * opts.durationMs
    }
}
