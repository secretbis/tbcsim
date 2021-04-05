package character.classes.hunter.abilities

import character.Ability
import character.Proc
import character.classes.hunter.talents.Efficiency
import character.classes.hunter.talents.TheBeastWithin
import data.Constants
import data.itemsets.GronnstalkersArmor
import data.itemsets.RiftStalkerArmor
import mechanics.General
import mechanics.Ranged
import sim.Event
import sim.SimParticipant
import kotlin.random.Random

class SteadyShot : Ability() {
    companion object {
        const val name = "Steady Shot"
    }
    override val id: Int = 34120
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
//    override fun castTimeMs(sp: SimParticipant): Int = (1000 / sp.physicalHasteMultiplier()).toInt()
    override fun castTimeMs(sp: SimParticipant): Int = 1000
    val baseCost = 110.0
    override fun resourceCost(sp: SimParticipant): Double {
        val efficiency = sp.character.klass.talents[Efficiency.name] as Efficiency?
        val effDiscount = efficiency?.shotManaCostReduction() ?: 0.0

        val tbwDiscount = if(sp.buffs[TheBeastWithin.name] != null) { 0.2 } else 0.0

        return General.resourceCostReduction(baseCost, listOf(effDiscount, tbwDiscount))
    }

    val flatBonusDmg = 150.0
    override fun cast(sp: SimParticipant) {
        val item = sp.character.gear.rangedTotemLibram

        val t5Bonus = sp.buffs[RiftStalkerArmor.FOUR_SET_BUFF_NAME] != null
        val t5BonusCrit = if(t5Bonus) { RiftStalkerArmor.fourSetSteadyShotBonusCritPct() } else 0.0

        val t6Bonus = sp.buffs[GronnstalkersArmor.FOUR_SET_BUFF_NAME] != null
        val t6BonusMultiplier = if(t6Bonus) { GronnstalkersArmor.fourSetSteadyShotDamageMultiplier() } else 1.0

        // This shot does not benefit from ammo DPS
        // TODO: This was *technically* a bug, so we should keep an eye on testing
        val damage = Random.nextDouble(item.minDmg, item.maxDmg) + flatBonusDmg + (sp.rangedAttackPower() * 0.2) * t6BonusMultiplier
        val result = Ranged.attackRoll(sp, damage, item, bonusCritChance = t5BonusCrit)

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
            Event.Result.HIT -> listOf(Proc.Trigger.RANGED_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.RANGED_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.MISS -> listOf(Proc.Trigger.RANGED_MISS)
            Event.Result.BLOCK -> listOf(Proc.Trigger.RANGED_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.BLOCKED_CRIT -> listOf(Proc.Trigger.RANGED_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item), this, event)
        }
    }
}
