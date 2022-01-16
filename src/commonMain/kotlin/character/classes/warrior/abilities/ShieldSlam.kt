package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.FocusedRage
import character.classes.warrior.talents.ImprovedSlam
import data.Constants
import data.itemsets.OnslaughtArmor
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant
import kotlin.random.Random

class ShieldSlam : Ability() {
    companion object {
        const val name = "Shield Slam"
    }

    override val id: Int = 30356
    override val name: String = Companion.name
    override val icon: String = "inv_shield_05.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 6000

    override fun available(sp: SimParticipant): Boolean {
        val isDefensiveStance = sp.buffs[DefensiveStance.name] != null
        val hasShield = sp.character.gear.offHand.itemSubclass == Constants.ItemSubclass.SHIELD
        return isDefensiveStance && hasShield && super.available(sp)
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double {
        val focusedRageRanks = sp.character.klass.talents[FocusedRage.name]?.currentRank ?: 0
        return 20.0 - focusedRageRanks
    }

    val baseDamage = Pair(420.0, 440.0)
    override fun cast(sp: SimParticipant) {
        val item = sp.character.gear.mainHand

        val t6Multiplier = if(sp.buffs[OnslaughtArmor.FOUR_SET_BUFF_NAME] != null) {
            OnslaughtArmor.fourSetShieldSlamMultiplier()
        } else 1.0
        val bonusDmg = sp.blockValue()

        val damageRoll = (Melee.baseDamageRoll(sp, item, isNormalized = false) + bonusDmg) * t6Multiplier
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false)

        // Save last hit state and fire event
        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
            abilityBonusThreat = 305.0
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
        }
    }
}
