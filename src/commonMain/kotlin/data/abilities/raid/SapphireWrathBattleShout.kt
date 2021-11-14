package data.abilities.raid

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import sim.SimParticipant

class SapphireWrathBattleShout : Ability() {
    companion object {
        const val name = "Improved Battle Shout (Sapphire + 3pc T2)"
    }

    override val id: Int = 2048
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    // Always assume the raid buffer has 5/5 imp BS
    val bonusAp = (305.0 * 1.25) + 100
    val buff = object : Buff() {
        override val name: String = Companion.name
        // Technically, this won't last more than 2 minutes, but these kinds of gamers love looking at inflated numbers
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override val mutex: List<Mutex> = listOf(Mutex.BUFF_BATTLE_SHOUT)
        override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
            return mapOf(
                Mutex.BUFF_BATTLE_SHOUT to bonusAp.toInt()
            )
        }

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPower = bonusAp.toInt()
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
