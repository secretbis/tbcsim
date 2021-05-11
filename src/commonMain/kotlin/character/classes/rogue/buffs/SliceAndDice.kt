package character.classes.rogue.buffs

import character.*
import character.classes.rogue.abilities.*
import character.classes.rogue.talents.*
import data.Constants
import sim.Event
import sim.SimParticipant
import data.model.Item
import mechanics.Rating
import data.itemsets.Netherblade
import data.itemsets.SlayersArmor

class SliceAndDice(sp: SimParticipant, consumedComboPoints: Int) : Buff() {
    companion object {
        const val name = "Slice and Dice"
    }

    override val name: String = "Slice and Dice"        
    override val durationMs: Int = getDurationForCombopoints(sp, consumedComboPoints)

    // can only replace when the new buff has a longer remaining duration
    override val mutex: List<Mutex> = listOf(Mutex.BUFF_SLICE_AND_DICE)
    override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
        return mapOf(
            Mutex.BUFF_SLICE_AND_DICE to this.remainingDurationMs(sp)
        )
    }

    val hastePercent = 30.0

    fun getDurationForCombopoints(sp: SimParticipant, consumedComboPoints: Int): Int {

        val netherblade = sp.buffs[Netherblade.TWO_SET_BUFF_NAME]
        val durationBonus = if (netherblade != null) { Netherblade.twoSetSnDDurationIncreaseMs() } else 0

        val baseDuration: Int = when(consumedComboPoints){
            1 -> 9000 + durationBonus
            2 -> 12000 + durationBonus
            3 -> 15000 + durationBonus
            4 -> 18000 + durationBonus
            5 -> 21000 + durationBonus
            else -> 0
        }

        val improved = sp.character.klass.talents[ImprovedSliceAndDice.name] as ImprovedSliceAndDice?
        val multiplier = improved?.durationMultiplier() ?: 1.0
        
        return (baseDuration * multiplier).toInt()
    }
    
    override fun modifyStats(sp: SimParticipant): Stats {  
        val slayers = sp.buffs[Netherblade.TWO_SET_BUFF_NAME]
        val hasteBonus = if (slayers != null) { SlayersArmor.twoSetAdditionalSnDHaste() } else 0.0

        // TODO: might be multiplicative
        return Stats(
            physicalHasteMultiplier = 1.3 + hasteBonus
        )
    }
}