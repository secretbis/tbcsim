package character.classes.shaman.buffs

import character.Ability
import character.Proc
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimIteration

class FlametongueWeapon : Ability() {
    companion object {
        const val name = "Flametongue Weapon"
    }
    override val id: Int = 25489
    override val name: String = Companion.name

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    // Per internet anedcodes, this gets 10% of spell power
    val spCoeff = 0.10
    val baseDamage = Pair(0.0, 1.0)
    override fun cast(sim: SimIteration, free: Boolean) {
        val school = Constants.DamageType.FIRE
        val damageRoll = Spell.baseDamageRoll(sim, baseDamage.first, baseDamage.second, spCoeff, school)
        val result = Spell.attackRoll(sim, damageRoll, school)

        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        ))

        // Proc anything that can proc off Nature damage
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.FIRE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.FIRE_DAMAGE)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.FIRE_DAMAGE)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.FIRE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(), this)
        }
    }

    override val baseCastTimeMs: Int = 0
    override fun gcdMs(sim: SimIteration): Int = 0
}
