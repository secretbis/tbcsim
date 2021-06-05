package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import data.model.Item
import mu.KotlinLogging
import character.classes.rogue.talents.*

abstract class FinisherAbility : Ability() {

    protected var consumedComboPoints: Int = 0
    
    override open fun available(sp: SimParticipant): Boolean {
        // making sure at least 1 CP is available, but this should already be ensured in the rotation
        return sp.hasEnoughResource(Resource.Type.COMBO_POINT, 1.0) && super.available(sp)
    }

    override open fun beforeCast(sp: SimParticipant) {
        super.beforeCast(sp)
        consumedComboPoints = sp.resources[Resource.Type.COMBO_POINT]!!.currentAmount
        sp.subtractResource(consumedComboPoints, Resource.Type.COMBO_POINT, name)
    }

    protected fun fireProcAsFinisher(sp: SimParticipant, triggers: List<Proc.Trigger>?, items: List<Item>?, event: Event?) {
        val finalTriggers: MutableList<Proc.Trigger> = mutableListOf(Proc.Trigger.ROGUE_CAST_FINISHER)
        if(triggers != null) {
            finalTriggers.addAll(triggers)
        }
        
        sp.fireProc(finalTriggers, items, this, event)
    }

    protected fun noDodgeAllowed(sp: SimParticipant): Boolean {
        val sa = sp.character.klass.talents[SurpriseAttacks.name] as SurpriseAttacks?
        return if(sa != null){ sa.currentRank == sa.maxRank } else { false }
    }
}
