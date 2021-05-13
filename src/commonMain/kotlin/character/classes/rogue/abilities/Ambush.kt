package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import character.classes.rogue.talents.*
import character.classes.rogue.buffs.*
import mu.KotlinLogging

class Ambush : Ability() {
    companion object {
        const val name = "Ambush"
    }

    override val id: Int = 27441
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 60.0

    override fun available(sp: SimParticipant): Boolean {
        val usesDagger = sp.character.gear.mainHand.itemSubclass == Constants.ItemSubclass.DAGGER
        if (!usesDagger) {
            KotlinLogging.logger{}.debug{ "Tried to use ability $name without having a dagger in the mainhand" }
        }
        val inStealth = sp.buffs[Stealth.name] != null
        
        return inStealth && usesDagger && super.available(sp)
    }

    val bonusDamage = 335
    val weaponDamageMultiplier = 2.75
    override fun cast(sp: SimParticipant) {

        var increasedDamagePercent = 0.0
        val opportunity = sp.character.klass.talents[Opportunity.name] as Opportunity?
        increasedDamagePercent += opportunity?.damageIncreasePercent() ?: 0.0
        
        val dmgMultiplier = 1 + (increasedDamagePercent / 100.0).coerceAtLeast(0.0)
        val finalMultiplier = weaponDamageMultiplier * dmgMultiplier

        val improved = sp.character.klass.talents[ImprovedAmbush.name] as ImprovedAmbush?
        val critChanceIncrease = improved?.bonusCritChance() ?: 0.0


        val item = sp.character.gear.mainHand
        // TODO: not sure if correct
        val damageRoll = (Melee.baseDamageRoll(sp, item, isNormalized = true) * finalMultiplier) + bonusDamage
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false, bonusCritChance = critChanceIncrease)
        
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
