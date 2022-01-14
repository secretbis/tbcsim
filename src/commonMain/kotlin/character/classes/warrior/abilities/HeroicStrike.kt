package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.FocusedRage
import character.classes.warrior.talents.ImprovedHeroicStrike
import data.Constants
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class HeroicStrike : Ability() {
    companion object {
        const val name = "Heroic Strike"
    }

    override val id: Int = 29707
    //Rank 11 Heroic Strike is unavailable to players despite being on the DB
    override val name: String = Companion.name
    override val icon: String = "ability_rogue_ambush.jpg"

    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd: Boolean = true

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double {
        val impHsRanks = sp.character.klass.talents[ImprovedHeroicStrike.name]?.currentRank ?: 0
        val focusedRageRanks = sp.character.klass.talents[FocusedRage.name]?.currentRank ?: 0
        return 15.0 - impHsRanks - focusedRageRanks
    }

    // Model the off-hand hit bonus when HS is queued
    val offHandHitBuff = object : Buff() {
        override val name: String = "Heroic Strike (Queued)"
        override val icon: String = "ability_rogue_ambush.jpg"
        override val durationMs: Int = -1
        override val maxCharges: Int = 1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(offHandAddlWhiteHitPct = 19.0)
        }
    }

    val bonusDamage = 176.0
    val nextHitAbility = object : Ability() {
        override val id: Int = 29707
        override val name: String = Companion.name
        override val icon: String = "ability_rogue_ambush.jpg"

        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            sp.consumeBuff(offHandHitBuff)

            val mhItem = sp.character.gear.mainHand
            val damage = Melee.baseDamageRoll(sp, mhItem, isNormalized = false) + bonusDamage
            val result = Melee.attackRoll(sp, damage, mhItem, isWhiteDmg = false)

            // Save last hit state and fire event
            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                ability = this,
                amount = result.first,
                result = result.second,
                abilityBonusThreat = 194.0
            )
            sp.logEvent(event)

            // Proc anything that can proc off a yellow hit or a replaced auto-attack hit
            val triggerTypes = when(result.second) {
                EventResult.HIT -> listOf(Proc.Trigger.MELEE_REPLACED_AUTO_ATTACK_HIT, Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                EventResult.CRIT -> listOf(Proc.Trigger.MELEE_REPLACED_AUTO_ATTACK_CRIT, Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
                EventResult.MISS -> listOf(Proc.Trigger.MELEE_MISS)
                EventResult.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
                EventResult.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
                EventResult.BLOCK -> listOf(Proc.Trigger.MELEE_REPLACED_AUTO_ATTACK_HIT, Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_REPLACED_AUTO_ATTACK_CRIT, Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
                else -> null
            }

            if(triggerTypes != null) {
                sp.fireProc(triggerTypes, listOf(mhItem), this, event)
            }
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.replaceNextMainHandAutoAttack(nextHitAbility)
        if(sp.isDualWielding()) {
            sp.addBuff(offHandHitBuff)
        }
    }
}
