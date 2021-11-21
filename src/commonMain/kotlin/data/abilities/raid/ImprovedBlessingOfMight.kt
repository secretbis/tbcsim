package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class ImprovedBlessingOfMight : Ability() {
    companion object {
        const val name = "Improved Blessing of Might"
    }

    override val id: Int = 27140
    override val name: String = Companion.name
    override val icon: String = "spell_holy_greaterblessingofkings.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    // Always assume the raid buffer has 5/5 imp might
    val bonusAp = 220.0 * 1.2
    val buff = object : Buff() {
        override val name: String = "Blessing of Might"
        override val icon: String = "spell_holy_greaterblessingofkings.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPower = bonusAp.toInt(),
                rangedAttackPower = bonusAp.toInt()
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
