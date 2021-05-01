package character.classes.rogue.debuffs

import character.Ability
import character.*
import character.classes.rogue.talents.*
import data.Constants
import sim.Event
import sim.SimParticipant
import character.Proc

class ExposeArmor(owner: SimParticipant, consumedComboPoints: Int) : Debuff(owner) {
    companion object {
        const val name = "Expose Armor"
    }

    override val name: String = "Expose Armor"
    override val durationMs: Int = 30000

    val armorReduce = getArmorReduceForCombopoints(owner, consumedComboPoints)

    override val mutex: List<Mutex> = listOf(Mutex.DEBUFF_EXPOSE_ARMOR)
    override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
        return mapOf(
            Mutex.DEBUFF_EXPOSE_ARMOR to armorReduce
        )
    }

    override fun modifyStats(sp: SimParticipant): Stats {  
        return Stats(
            armor = -1 * armorReduce
        )
    }  

    fun getArmorReduceForCombopoints(sp: SimParticipant, consumedComboPoints: Int): Int {
        val improved = sp.character.klass.talents[ImprovedExposeArmor.name] as ImprovedExposeArmor?
        val multiplier = improved?.exposeArmorMultiplier() ?: 0.0
        val armorReduce: Int = when(consumedComboPoints){
            1 -> 410
            2 -> 820
            3 -> 1230
            4 -> 1640
            5 -> 2050
            else -> 0
        }
        return (armorReduce*multiplier).toInt()
    }
}
