package character.classes.hunter.abilities

import character.Ability
import character.Proc
import character.classes.hunter.talents.Efficiency
import character.classes.hunter.talents.ImprovedArcaneShot
import character.classes.hunter.talents.TheBeastWithin
import data.Constants
import mechanics.General
import mechanics.Ranged
import sim.Event
import sim.SimParticipant
import kotlin.random.Random

class ArcaneShot : Ability() {
    companion object {
        const val name = "Arcane Shot"
    }
    override val id: Int = 27019
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    val baseCd = 6000
    override fun cooldownMs(sp: SimParticipant): Int {
        val impArcaneRanks = sp.character.klass.talents[ImprovedArcaneShot.name]?.currentRank ?: 0
        return baseCd - (1000 * impArcaneRanks)
    }

    val baseCost = 230.0
    override fun resourceCost(sp: SimParticipant): Double {
        val efficiency = sp.character.klass.talents[Efficiency.name] as Efficiency?
        val effDiscount = efficiency?.shotManaCostReduction() ?: 0.0

        val tbwDiscount = if(sp.buffs[TheBeastWithin.name] != null) { 0.2 } else 0.0

        return General.resourceCostReduction(baseCost, listOf(effDiscount, tbwDiscount))
    }

    val flatBonusDmg = 273.0
    override fun cast(sp: SimParticipant) {
        val item = sp.character.gear.rangedTotemLibram
        // TODO: Spellpower doesn't affect this anymore right?
        // Ammo does not affect this shot
        val damage = Random.nextDouble(item.minDmg, item.maxDmg) + flatBonusDmg + (sp.rangedAttackPower() * 0.15)
        val result = Ranged.attackRoll(sp, damage, item)

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.ARCANE,
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
