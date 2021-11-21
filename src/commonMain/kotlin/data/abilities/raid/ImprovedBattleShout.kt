package data.abilities.raid

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import sim.SimParticipant

class ImprovedBattleShout : Ability() {
    companion object {
        const val name = "Improved Battle Shout"
    }

    override val id: Int = 2048
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_battleshout.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    // Always assume the raid buffer has 5/5 imp BS
    val bonusAp = 305.0 * 1.25
    val buff = object : Buff() {
        override val name: String = "Battle Shout"
        override val icon: String = "ability_warrior_battleshout.jpg"
        // Assume the caster is always maintaining this
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
