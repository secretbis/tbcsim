package data.abilities.raid

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import sim.SimParticipant
import kotlin.math.pow

class FerociousInspiration(val count: Int) : Ability() {
    override val id: Int = 34460
    override val name: String = "Ferocious Inspiration (x$count)"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Ferocious Inspiration (x$count)"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = false

        override val mutex: List<Mutex> = listOf(Mutex.BUFF_FEROCIOUS_INSPIRATION)
        override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
            return mapOf(
                Mutex.BUFF_FEROCIOUS_INSPIRATION to count
            )
        }

        // TODO: What's the typical uptime on this?  Currently assumes 100% uptime
        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                physicalDamageMultiplier = 1.03.pow(count),
                spellDamageMultiplier = 1.03.pow(count)
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
