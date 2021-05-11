package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import data.model.Item
import character.classes.rogue.talents.*
import character.classes.rogue.debuffs.*
import mechanics.Rating
import mechanics.Spell
import mu.KotlinLogging
import kotlin.math.*
import data.itemsets.AssassinationArmor
import data.itemsets.Deathmantle

class Envenom : FinisherAbility() {
    companion object {
        const val name = "Envenom"
    }

    override val id: Int = 32684
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double {
        val assArmor = sp.buffs[AssassinationArmor.FOUR_SET_BUFF_NAME]
        var reduction = if (assArmor != null) { AssassinationArmor.fourSetEnergyReduction() } else 0.0

        return 35.0 - reduction
    }

    override fun available(sp: SimParticipant): Boolean {
        // there has to be at least one deadly poison debuff available
        val dosesAvailable = sp.sim.target.debuffState[DeadlyPoisonDot.name] != null
        
        return dosesAvailable && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {

        val deathmantle = sp.buffs[Deathmantle.TWO_SET_BUFF_NAME]
        val bonusDamagePerCP = if (deathmantle != null) { Deathmantle.twoSetBonusDamagePerCP() } else 0.0

        val consumedPoisonDoses = min(maxPoisonDosesAvailable(sp), consumedComboPoints)
        val damage: Double = when(consumedPoisonDoses){
            1 -> 180 * 1 + sp.attackPower() * 0.03 + (bonusDamagePerCP * 1)
            2 -> 180 * 1 + sp.attackPower() * 0.06 + (bonusDamagePerCP * 2)
            3 -> 180 * 1 + sp.attackPower() * 0.09 + (bonusDamagePerCP * 3)
            4 -> 180 * 1 + sp.attackPower() * 0.12 + (bonusDamagePerCP * 4)
            5 -> 180 * 1 + sp.attackPower() * 0.15 + (bonusDamagePerCP * 5)
            else -> 0.0
        }

        val vp = sp.character.klass.talents[VilePoisons.name] as VilePoisons?
        val dmgIncrease = vp?.damageIncreasePercentEnvenom() ?: 0.0

        val dmgMultiplier = 1 + (dmgIncrease / 100.0).coerceAtLeast(0.0)

        // TODO: not sure if this really is just a flat increase on the final damage
        // TODO: this needs to be "cast" for resistance purposes, but use melee hit/crit
        val damageRoll = damage * dmgMultiplier
        val result = Melee.attackRoll(sp, damageRoll, null, isWhiteDmg = false, noDodgeAllowed = noDodgeAllowed(sp))

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.NATURE,
            abilityName = name,
            comboPointsSpent = consumedComboPoints,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        if(result.second != Event.Result.MISS && result.second != Event.Result.DODGE) {
            removePoisonStacks(sp, consumedPoisonDoses)
        }

        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            Event.Result.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            Event.Result.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            Event.Result.BLOCK -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.NATURE_DAMAGE)
            else -> null
        }

        fireProcAsFinisher(sp, triggerTypes, null, event)
    }

    fun maxPoisonDosesAvailable(sp: SimParticipant): Int {
        val debuffState = sp.sim.target.debuffState[DeadlyPoisonDot.name]
        if(debuffState == null) {
            KotlinLogging.logger{}.debug{ "Tried to cast $name but there was no deadly poison debuff present" }
            return 0
        }
        return debuffState.currentStacks
    }

    fun removePoisonStacks(sp: SimParticipant, stacks: Int) {
        val debuffState = sp.sim.target.debuffState[DeadlyPoisonDot.name]
        if(debuffState == null) {
            KotlinLogging.logger{}.debug{ "Tried to cast $name but there was no deadly poison debuff present" }
            return
        }
        
        if((debuffState.currentStacks) > stacks) {
            debuffState.currentStacks -= stacks
        } else {
            sp.sim.target.consumeDebuff(sp.sim.target.debuffs[DeadlyPoisonDot.name]!!)
        }
    }
}
