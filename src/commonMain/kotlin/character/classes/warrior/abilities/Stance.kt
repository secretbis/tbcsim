package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.TacticalMastery
import sim.SimParticipant
import kotlin.math.max

abstract class Stance: Ability() {
    // Stances have a shared cooldown equal to the physical GCD, but are not on the main physical GCD
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override val sharedCooldown: SharedCooldown = SharedCooldown.WARRIOR_STANCE

    fun stanceBuff(type: String, icon: String, stats: Stats = Stats()): Buff {
        return object : Buff() {
            override val name: String = type
            override val icon: String = icon
            override val durationMs: Int = -1

            override val mutex: List<Mutex> = listOf(Mutex.BUFF_WARRIOR_STANCE)

            override fun modifyStats(sp: SimParticipant): Stats {
                return stats
            }
        }
    }

    override fun cast(sp: SimParticipant) {
        // Reset rage according to talents
        val baseRageRetained = 10
        val tacticalMastery = sp.character.klass.talents[TacticalMastery.name] as TacticalMastery?

        val maxRageRetained = baseRageRetained + (tacticalMastery?.rageRetained() ?: 0)
        val currentRage = sp.resources[Resource.Type.RAGE]?.currentAmount ?: 0
        val rageLost = max(currentRage - maxRageRetained, 0)
        if(rageLost > 0) {
            sp.subtractResource(rageLost, Resource.Type.RAGE, this)
        }
    }
}
