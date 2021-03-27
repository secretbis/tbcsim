package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class ImprovedBattleShout : Ability() {
    companion object {
        const val name = "Improved Battle Shout"
    }

    override val id: Int = 2048
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    // Always assume the raid buffer has 5/5 imp BS
    val bonusAp = 305.0 * 1.25
    val buff = object : Buff() {
        override val name: String = "Battle Shout"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(attackPower = bonusAp.toInt())
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
