package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.FocusedRage
import character.classes.warrior.talents.ImprovedMortalStrike
import character.classes.warrior.talents.ImprovedSunderArmor
import character.classes.warrior.talents.Devastate as DevastateTalent
import data.Constants
import data.abilities.raid.ImprovedExposeArmor
import data.abilities.raid.SunderArmor
import data.itemsets.OnslaughtBattlegear
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class Devastate : Ability() {
    companion object {
        const val name = "Devastate"
    }

    override val id: Int = 30022
    override val name: String = Companion.name
    override val icon: String = "inv_sword_11.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 0

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE

    override fun resourceCost(sp: SimParticipant): Double {
        val focusedRageRanks = sp.character.klass.talents[FocusedRage.name]?.currentRank ?: 0
        val impSunderRanks = sp.character.klass.talents[ImprovedSunderArmor.name]?.currentRank ?: 0
        return 15.0 - focusedRageRanks - impSunderRanks
    }

    override fun available(sp: SimParticipant): Boolean {
        val hasTalent = sp.character.klass.talents[DevastateTalent.name]?.currentRank == 1
        val oneHandWeapon = Melee.is1H(sp.character.gear.mainHand)
        return hasTalent && oneHandWeapon && super.available(sp)
    }

    // Assume 5 sunders
    val bonusDmg = 35.0 * 5
    override fun cast(sp: SimParticipant) {
        val item = sp.character.gear.mainHand
        val damageRoll = (Melee.baseDamageRoll(sp, item, isNormalized = true) * 0.5) + bonusDmg
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false)

        // https://github.com/magey/tbc-warrior/wiki/Threat-Values#devastate
        val hasImpEa = sp.sim.target.debuffs[ImprovedExposeArmor.name] != null
        val sunderStacks = sp.sim.target.debuffs[SunderArmor.name]?.state(sp.sim.target)?.currentStacks ?: 0
        val sunderBonus = if(!hasImpEa && sunderStacks < 5) { 301.5 } else 0.0

        // Save last hit state and fire event
        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
            // Devastate bonus + sunder effect
            abilityBonusThreat = 100 + sunderBonus
        )
        sp.logEvent(event)

        // Proc anything that can proc off a yellow hit
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            EventResult.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            EventResult.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            EventResult.BLOCK -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item), this, event)
            // The sunder effect also procs yellow hit effects (confirmed?)
            sp.fireProc(listOf(Proc.Trigger.MELEE_YELLOW_HIT), listOf(item), this, event)
        }
    }
}
