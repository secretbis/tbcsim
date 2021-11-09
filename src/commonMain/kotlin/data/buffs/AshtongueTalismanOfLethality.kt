package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant
import kotlin.random.Random

class AshtongueTalismanOfLethality : Buff() {
    companion object {
        const val name = "Ashtongue Talisman of Lethality"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 10000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                meleeCritRating = 145.0
            )
        }
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.ROGUE_CAST_FINISHER)
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            val cpSpent = event?.comboPointsSpent ?: 0
            val chance = 0.2 * cpSpent
            if(Random.nextDouble() <= chance) {
                sp.addBuff(buff)
            }
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
