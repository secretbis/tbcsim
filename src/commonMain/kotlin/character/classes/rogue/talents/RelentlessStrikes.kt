package character.classes.rogue.talents

import character.*
import sim.SimParticipant
import data.model.Item
import sim.Event
import kotlin.random.Random

class RelentlessStrikes(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Relentless Strikes"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1

    fun chancePerComboPointPercent(): Double {
        return currentRank * 20.0
    }

    fun energyRestored(): Int = 25

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.ROGUE_CAST_FINISHER
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                val cpSpent = event?.comboPointsSpent
                if(cpSpent != null) {
                    val chance = chancePerComboPointPercent() * cpSpent
                    if(Random.nextDouble() <= chance) {
                        sp.addResource(energyRestored(), Resource.Type.ENERGY, name)
                    }
                } else {
                    logger.debug{ "event in proc of type ROGUE_CAST_FINISHER didn't include comboPointsSpent" }
                }
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}