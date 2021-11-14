package data.abilities.raid

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import sim.SimParticipant

class ImprovedHuntersMark : Ability() {
    companion object {
        const val name = "Improved Hunter's Mark"
    }

    override val id: Int = 2048
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    // Always assume the raid buffer has 5/5
    val bonusAp = 440
    val buff = object : Buff() {
        override val name: String = "Improved Hunter's Mark"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override val mutex: List<Mutex> = listOf(Mutex.BUFF_HUNTERS_MARK)
        override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
            return mapOf(
                Mutex.BUFF_HUNTERS_MARK to 440
            )
        }

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                // Imp Mark says "base" attack power
                attackPower = bonusAp / 4,
                rangedAttackPower = bonusAp
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
