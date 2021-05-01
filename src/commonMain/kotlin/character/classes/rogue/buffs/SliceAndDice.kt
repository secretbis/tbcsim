package character.classes.rogue.buffs

import character.*
import character.classes.rogue.abilities.*
import character.classes.rogue.talents.*
import data.Constants
import sim.Event
import sim.SimParticipant
import data.model.Item
import mechanics.Rating

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
        val baseDuration: Int = when(consumedComboPoints){
            1 -> 9000
            2 -> 12000
            3 -> 15000
            4 -> 18000
            5 -> 21000
            else -> 0
        }

        val improved = sp.character.klass.talents[ImprovedSliceAndDice.name] as ImprovedSliceAndDice?
        val multiplier = improved?.durationMultiplier() ?: 1.0
        
        return (baseDuration * multiplier).toInt()
    }
    
    override fun modifyStats(sp: SimParticipant): Stats {  
        val hasteRating = Rating.hastePerPct * hastePercent
        return Stats(
            physicalHasteRating = hasteRating
        )
    
        // above or below? difference? Troll racial berserking uses the above, but I think it should be the following
    
        //return Stats(
        //    physicalHasteMultiplier = 1.3
        //)
    }
}