package character.classes.rogue.debuffs

import character.Ability
import character.*
import data.Constants
import sim.Event
import sim.SimParticipant
import character.Proc

class RuptureDot(owner: SimParticipant, consumedComboPoints: Int) : Debuff(owner) {
    companion object {
        const val name = "Garrote (DoT)"
    }

    override val name: String = "${Companion.name} (DoT)"
    override val durationMs: Int = getDurationForCombopoints(owner, consumedComboPoints)
    override val tickDeltaMs: Int = 2000
    
    val dmgPerTick = getDamageForCombopoints(owner, consumedComboPoints) / (durationMs / tickDeltaMs)

    // can only replace if it has higher dmg
    override val mutex: List<Mutex> = listOf(Mutex.DEBUFF_RUPTURE_DOT)
    override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
        return mapOf(
            Mutex.DEBUFF_RUPTURE_DOT to dmgPerTick.toInt()
        )
    }

    fun getDurationForCombopoints(sp: SimParticipant, consumedComboPoints: Int): Int {
        val timeMs: Int = when(consumedComboPoints){
            1 -> 8
            2 -> 10
            3 -> 12
            4 -> 14
            5 -> 16
            else -> 0
        }
        return timeMs
    }

    fun getDamageForCombopoints(sp: SimParticipant, consumedComboPoints: Int): Double {
        val damage: Double = when(consumedComboPoints){
            1 -> 324.0
            2 -> 460.0
            3 -> 618.0
            4 -> 798.0
            5 -> 1000.0
            else -> 0.0
        }
        return damage
    }
        
    val dot = object : Ability() {
        override val id: Int = 26867
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0
        
        override fun cast(sp: SimParticipant) {
            val event = Event(
                eventType = Event.Type.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                abilityName = name,
                amount = dmgPerTick,
                result = Event.Result.HIT,
            )
            owner.logEvent(event)
        
            owner.fireProc(listOf(Proc.Trigger.PHYSICAL_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }
        
    override fun tick(sp: SimParticipant) {
        dot.cast(owner)
    }
}
