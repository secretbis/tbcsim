package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import data.model.Item
import character.classes.rogue.talents.*
import character.classes.rogue.buffs.*
import mechanics.Rating
import sim.EventType

class SliceAndDice : FinisherAbility() {
    companion object {
        const val name = "Slice and Dice"
    }

    override val id: Int = 6774
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 25.0

//    TODO: This has a very large performance impact - the rotation should handle all of these cases, practically speaking
//    override fun available(sp: SimParticipant): Boolean {
//        // make sure we are allowed to replace existing buff
//        val wouldBeCombopoints = sp.resources[Resource.Type.COMBO_POINT]!!.currentAmount
//        val wouldBeBuff = character.classes.rogue.buffs.SliceAndDice(sp, wouldBeCombopoints)
//        val canAddBuff = sp.shouldApplyBuff(wouldBeBuff, sp.buffs)
//
//        return canAddBuff && super.available(sp)
//    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(character.classes.rogue.buffs.SliceAndDice(sp, consumedComboPoints))

        val event = Event(
            eventType = EventType.SPELL_CAST,
            comboPointsSpent = consumedComboPoints
        )
        fireProcAsFinisher(sp, null, null, event)
    }
}
