package data.abilities.raid

import character.*
import sim.SimParticipant

class SunderArmor : Ability() {
    companion object {
        const val name = "Sunder Armor"
    }

    override val id: Int = 25225
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_sunder.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    fun debuff(owner: SimParticipant) = object : Debuff(owner) {
        override val name: String = "Sunder Armor"
        override val icon: String = "ability_warrior_sunder.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = false
        override val maxStacks: Int = 5

        override val mutex: List<Mutex> = listOf(Mutex.DEBUFF_MAJOR_ARMOR)
        override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> = mapOf(
            Mutex.DEBUFF_MAJOR_ARMOR to (5 * 520)
        )

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(armor = -1 * 5 * 520)
        }
    }

    override fun cast(sp: SimParticipant) {
        val debuff = debuff(sp)
        debuff.state(sp.sim.target).currentStacks = 5
        sp.sim.target.addDebuff(debuff)
    }
}
