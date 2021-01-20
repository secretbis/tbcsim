package character.classes.shaman.abilities

import character.Ability
import character.Proc
import character.classes.shaman.Shaman
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

class WindfuryWeapon(sim: SimIteration, val item: Item) : Ability(sim) {
    override val id: Int = 25505
    override val name: String = "Windfury Weapon (Rank 5)"

    // Windfury weapon has a global 3s ICD, regardless of rank
    val icdMs = 3000
    override fun available(): Boolean {
        val lastProc = sim.lastWindfuryWeaponProcMs
        return lastProc == -1 || lastProc + icdMs <= sim.elapsedTimeMs
    }

    val extraAp = 445
    override fun cast(free: Boolean) {
        val attackOne = Melee.baseDamageRoll(sim, item, extraAp)
        val attackTwo = Melee.baseDamageRoll(sim, item, extraAp)
        val result = Melee.attackRoll(sim, attackOne + attackTwo, true)

        (sim.subject.klass as Shaman).lastWindfuryWeaponProcMs = sim.elapsedTimeMs
        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
        ))

        // Proc anything that can proc off a white hit
        // TODO: Procs off miss/dodge/parry/etc?
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_WHITE_HIT)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_WHITE_CRIT)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(item), this)
        }
    }

    override fun castTimeMs(): Int = 0
    override fun gcdMs(): Int = 0
}
