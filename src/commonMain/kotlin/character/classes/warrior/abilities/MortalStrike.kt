package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedMortalStrike
import character.classes.warrior.talents.MortalStrike as MortalStrikeTalent
import data.Constants
import data.itemsets.OnslaughtBattlegear
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class MortalStrike : Ability() {
    companion object {
        const val name = "Mortal Strike"
    }

    override val id: Int = 30330
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_savageblow.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun cooldownMs(sp: SimParticipant): Int {
        val impMSRanks = sp.character.klass.talents[ImprovedMortalStrike.name]?.currentRank ?: 0
        val discount = 200 * impMSRanks
        return 6000 - discount
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double = 30.0

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.klass.talents[MortalStrikeTalent.name]?.currentRank == 1 && super.available(sp)
    }

    val bonusDmg = 210.0
    override fun cast(sp: SimParticipant) {
        val impMSRanks = sp.character.klass.talents[ImprovedMortalStrike.name]?.currentRank ?: 0
        val dmgMult = 1.0 + (0.01 * impMSRanks)

        // Check T6 set bonus
        val t6Bonus = sp.buffs[OnslaughtBattlegear.FOUR_SET_BUFF_NAME] != null
        val t6Multiplier = if(t6Bonus) { OnslaughtBattlegear.fourSetMSBTDamageMultiplier() } else 1.0

        val item = sp.character.gear.mainHand
        val damageRoll = (Melee.baseDamageRoll(sp, item, isNormalized = true) + bonusDmg) * dmgMult * t6Multiplier
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false)

        // Save last hit state and fire event
        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
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
            sp.fireProc(listOf(Proc.Trigger.WARRIOR_CAST_MORTAL_STRIKE) + triggerTypes, listOf(item), this, event)
        }
    }
}
