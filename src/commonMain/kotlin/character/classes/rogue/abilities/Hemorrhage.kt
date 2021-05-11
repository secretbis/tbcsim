package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import character.classes.rogue.talents.*
import character.classes.rogue.debuffs.*
import mu.KotlinLogging

class Hemorrhage : Ability() {
    companion object {
        const val name = "Hemorrhage"
    }

    override val id: Int = 26864
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 40.0

    override fun available(sp: SimParticipant): Boolean {
        val hemo = sp.character.klass.talents[character.classes.rogue.talents.Hemorrhage.name] as character.classes.rogue.talents.Hemorrhage?
        val available = if(hemo != null){ hemo.currentRank == hemo.maxRank } else { false }
        if (!available) {
            KotlinLogging.logger{}.debug{ "Tried to use ability $name without having the corresponding talent" }
        }

        return available && super.available(sp)
    }

    val weaponDamageMultiplier = 1.10
    override fun cast(sp: SimParticipant) {

        val lethality = sp.character.klass.talents[Lethality.name] as Lethality?
        val critDmgMultiplier = lethality?.critDamageMultiplier() ?: 1.0

        val item = sp.character.gear.mainHand
        val damageRoll = Melee.baseDamageRoll(sp, item, isNormalized = true) * weaponDamageMultiplier
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false, abilityAdditionalCritDamageMultiplier = critDmgMultiplier)
        
        if(result.second != Event.Result.MISS && result.second != Event.Result.DODGE) {
            sp.addResource(1, Resource.Type.COMBO_POINT, name)
        }

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        if(result.second != Event.Result.MISS && result.second != Event.Result.DODGE && result.second != Event.Result.PARRY) {
            sp.sim.target.addDebuff(character.classes.rogue.debuffs.Hemorrhage(sp))
        }

        // Proc anything that can proc off a yellow hit
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            Event.Result.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            Event.Result.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            Event.Result.BLOCK -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item), this, event)
        }
    }
}
