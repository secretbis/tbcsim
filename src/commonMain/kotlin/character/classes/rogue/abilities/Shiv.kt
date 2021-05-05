package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import character.classes.rogue.talents.*
import character.classes.rogue.buffs.*

class Shiv : Ability() {
    companion object {
        const val name = "Shiv"
    }

    override val id: Int = 5938
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double {
        return (sp.character.gear.offHand.speed/1000.0) * 10.0 + 20.0
    }

    override fun cast(sp: SimParticipant) {
        val lethality = sp.character.klass.talents[Lethality.name] as Lethality?
        val critDmgMultiplier = lethality?.critDamageMultiplier() ?: 1.0

        val surprise = sp.character.klass.talents[SurpriseAttacks.name] as SurpriseAttacks?
        val increasedDamagePercent = surprise?.damageIncreasePercent() ?: 0.0
        val dmgMultiplier = 1 + (increasedDamagePercent / 100.0).coerceAtLeast(0.0)

        val item = sp.character.gear.offHand
        val damageRoll = Melee.baseDamageRoll(sp, item, isNormalized = true) * dmgMultiplier
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false, abilityAdditionalCritDamageMultiplier = critDmgMultiplier)

        if(result.second != Event.Result.MISS && result.second != Event.Result.DODGE) {
            // TODO: unsure if best way to do this
            val poison = sp.character.gear.offHand.tempEnchant as? Poison
            if(poison != null) {
                poison.poisonAbility?.cast(sp)
            }
            
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
