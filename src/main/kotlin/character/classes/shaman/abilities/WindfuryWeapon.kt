package character.classes.shaman.abilities

import character.Ability
import character.Proc
import character.classes.shaman.talents.ElementalWeapons
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

class WindfuryWeapon(override val name: String, val item: Item) : Ability() {
    companion object {
        const val name = "Windfury Weapon"
    }

    override val id: Int = 25505

    override fun available(sim: SimIteration): Boolean {
        return if(isOffHand(sim)) { sim.subject.isDualWielding() } else true
    }

    fun isOffHand(sim: SimIteration): Boolean {
        return item === sim.subject.gear.offHand
    }

    val extraAp = 445
    override fun cast(sim: SimIteration, free: Boolean) {
        // Apply talents
        val elementalWeapons = sim.subject.klass.talents[ElementalWeapons.name] as ElementalWeapons?

        // Do attacks
        val attackOne = Melee.baseDamageRoll(sim, item, extraAp)
        val attackTwo = Melee.baseDamageRoll(sim, item, extraAp)

        // Per EJ, WF Weapon is yellow damage
        // https://web.archive.org/web/20080811084026/http://elitistjerks.com/f47/t15809-shaman_windfury/
        val initialResult = Melee.attackRoll(sim, attackOne + attackTwo, false, isOffHand(sim))

        // Apply the nuttiest talent ever made
        val elementalWeaponsMod = elementalWeapons?.windfuryApMultiplier() ?: 1.0
        val result = Pair(initialResult.first * elementalWeaponsMod, initialResult.second)

        // TODO: Is this considered one damage event or two, for the purposes of procs?
        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
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
    override fun gcdMs(sim: SimIteration): Int = 0
}
