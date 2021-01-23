package character.classes.shaman.abilities

import character.Ability
import character.Proc
import character.classes.shaman.talents.ElementalWeapons
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

class WindfuryWeapon(sim: SimIteration, val item: Item) : Ability(sim) {
    override val id: Int = 25505
    override val name: String
        get() {
            val suffix = if(isOffHand()) { "(OH)" } else { "(MH)" }
            return "Windfury Weapon $suffix"
        }

    override fun available(sim: SimIteration): Boolean {
        return if(isOffHand()) { sim.subject.isDualWielding() } else true
    }

    fun isOffHand(): Boolean {
        return item === sim.subject.gear.offHand
    }

    val baseExtraAp = 445
    override fun cast(free: Boolean) {
        // Apply talents
        val elementalWeapons = sim.subject.klass.talents[ElementalWeapons.name] as ElementalWeapons?
        val extraAp = (baseExtraAp * (elementalWeapons?.windfuryApMultiplier() ?: 1.0)).toInt()

        // Do attacks
        val attackOne = Melee.baseDamageRoll(sim, item, extraAp)
        val attackTwo = Melee.baseDamageRoll(sim, item, extraAp)
        val result = Melee.attackRoll(sim, attackOne + attackTwo, true, isOffHand())

        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
        ))

        // Proc anything that can proc off a white hit
        // TODO: Should I fire procs off miss/dodge/parry/etc?
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(item), this)
        }
    }

    override val baseCastTimeMs: Int = 0
    override val gcdMs: Int = 0
}
