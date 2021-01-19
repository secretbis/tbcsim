package character.auto

import character.Ability
import mechanics.Melee
import sim.Event
import sim.Sim
import kotlin.random.Random

abstract class MeleeBase(sim: Sim) : Ability(sim) {
    abstract fun getEventType(): Event.Type
    abstract fun getWeaponMin(): Double
    abstract fun getWeaponMax(): Double
    abstract fun getWeaponSpeed(): Double

    override fun castTimeMs(): Double = 0.0
    override fun gcdMs(): Double = 0.0

    var lastAttackTimeMs: Int = 0

    override fun available(): Boolean {
        val nextAvailableTimeMs = lastAttackTimeMs + getWeaponSpeed()
        return nextAvailableTimeMs <= sim.currentIteration.elapsedTimeMs
    }

    override fun cast(free: Boolean) {
        val hitRoll = Random.nextDouble()
        val damageRoll = Random.nextDouble(getWeaponMin(), getWeaponMax())

        // Generate a single roll table
        val missChance = Melee.getMeleeMissChance(sim, true)
        val dodgeChance = Melee.getMeleeDodgeChance(sim) + missChance
        val parryChance = Melee.getMeleeParryChance(sim) + dodgeChance
        val glanceChance = Melee.getMeleeGlanceChance(sim) + parryChance
        val blockChance = Melee.getMeleeBlockChance(sim) + glanceChance
        val critChance = Melee.getMeleeCritChance(sim) + blockChance

        val unmitigated = when {
            hitRoll < missChance -> Pair(0.0, Event.Result.MISS)
            hitRoll < dodgeChance -> Pair(0.0, Event.Result.DODGE)
            hitRoll < parryChance -> Pair(0.0, Event.Result.PARRY)
            hitRoll < glanceChance -> Pair(damageRoll * (1 - Melee.getMeleeGlanceReduction(sim)), Event.Result.GLANCE)
            hitRoll < blockChance -> Pair(damageRoll * (1 - Melee.getMeleeBlockReduction(sim)), Event.Result.BLOCK)
            hitRoll < critChance -> Pair(damageRoll * sim.subject.stats.whiteDamageCritMultiplier, Event.Result.CRIT)
            else -> Pair(damageRoll, Event.Result.HIT)
        }

        // Apply target armor mitigation
        val mitigated = Pair(unmitigated.first * (1 - Melee.getMeleeArmorMitigation(sim)), unmitigated.second)

        // Save last hit state and fire event
        lastAttackTimeMs = sim.currentIteration.elapsedTimeMs
        sim.logEvent(Event(
            type = getEventType(),
            amount = mitigated.first,
            result = mitigated.second,
        ))
    }
}
