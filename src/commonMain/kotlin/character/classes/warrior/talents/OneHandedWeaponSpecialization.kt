package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Melee
import sim.SimParticipant

class OneHandedWeaponSpecialization(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "One-Handed Weapon Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_sword_20.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return if(Melee.is1H(sp.character.gear.mainHand)) {
                val multiplier = 1.0 + currentRank * 0.02
                Stats(physicalDamageMultiplier = multiplier)
            } else Stats()
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
