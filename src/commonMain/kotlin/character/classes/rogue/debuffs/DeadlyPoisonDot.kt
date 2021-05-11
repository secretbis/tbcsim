package character.classes.rogue.debuffs

import character.Ability
import character.Debuff
import data.Constants
import sim.Event
import sim.SimParticipant
import character.Proc
import character.classes.rogue.talents.*

class DeadlyPoisonDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Deadly Poison (DoT)"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 12000
    override val tickDeltaMs: Int = 3000
    val totalTicks = durationMs / tickDeltaMs
    override val maxStacks: Int = 5

    fun dmgPerStack(sp: SimParticipant): Double {
        val vp = sp.character.klass.talents[VilePoisons.name] as VilePoisons?
        val dmgIncrease = vp?.damageIncreasePercentEnvenom() ?: 0.0

        val dmgMultiplier = 1 + (dmgIncrease / 100.0).coerceAtLeast(0.0)
        
        return 180.0 * dmgMultiplier      // VII, max at 70
        //return 108.0 * dmgMultiplier    // IV to test since its the one you get on beta premades
    }

    fun getAbility(debuff: Debuff): Ability {
        return object : Ability() {
            override val id: Int = 27187
            override val name: String = Companion.name
            override fun gcdMs(sp: SimParticipant): Int = 0
            override val castableOnGcd: Boolean = true
    
            override fun cast(sp: SimParticipant) {
                val stacks = sp.sim.target.debuffState[debuff.name]?.currentStacks ?: 0
                val dmgPerTick = ((stacks+1) * dmgPerStack(sp)) / totalTicks
                val event = Event(
                    eventType = Event.Type.DAMAGE,
                    damageType = Constants.DamageType.NATURE,
                    abilityName = name,
                    amount = dmgPerTick,
                    result = Event.Result.HIT
                )
                owner.logEvent(event)
    
                owner.fireProc(listOf(Proc.Trigger.NATURE_DAMAGE), listOf(), this, event)
            }
        }
    }

    val dotAbility = getAbility(this)

    override fun tick(sp: SimParticipant) {
        dotAbility.cast(owner)
    }
}
