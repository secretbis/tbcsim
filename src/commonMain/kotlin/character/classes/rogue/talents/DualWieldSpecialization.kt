package character.classes.rogue.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class DualWieldSpecialization(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Dual Wield Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun offhandDamageIncrease(): Double {
        return currentRank * 0.10
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val icon: String = "ability_dualwield.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                whiteDamageAddlOffHandPenaltyModifier = offhandDamageIncrease(),
                yellowDamageAddlOffHandPenaltyModifier = offhandDamageIncrease()
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
