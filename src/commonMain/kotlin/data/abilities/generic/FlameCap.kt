package data.abilities.generic

import character.*
import data.Constants
import data.model.Item
import mechanics.Spell
import sim.Event
import sim.EventType
import sim.SimParticipant

class FlameCap : Ability() {
    companion object {
        const val name = "Flame Cap"
        const val icon: String = "inv_misc_herb_flamecap.jpg"
    }

    override val id: Int = 22788
    override val name: String = Companion.name
    override val icon: String = Companion.icon
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd = true
    override val sharedCooldown: SharedCooldown = SharedCooldown.RUNE_OR_MANA_GEM
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    val fireAbility = object : Ability() {
        override val id: Int = 28715
        override fun gcdMs(sp: SimParticipant): Int = 0
        override val name: String = "Flamecap Fire"
        override val icon: String = "spell_fire_flameshock.jpg"

        val school = Constants.DamageType.FIRE
        override fun cast(sp: SimParticipant) {
            val damageRoll = Spell.baseDamageRollSingle(sp, 40.0, school, 0.0)

            val result = Spell.attackRoll(sp, damageRoll, school)
            val damageEvent = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                ability = this,
                amount = result.first,
                result = result.second,
            )
            sp.logEvent(damageEvent)
        }
    }

    val fireProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT,
            Trigger.MELEE_BLOCK,
            Trigger.MELEE_GLANCE,
            Trigger.RANGED_AUTO_HIT,
            Trigger.RANGED_AUTO_CRIT,
            Trigger.RANGED_WHITE_HIT,
            Trigger.RANGED_WHITE_CRIT,
            Trigger.RANGED_YELLOW_HIT,
            Trigger.RANGED_YELLOW_CRIT,
            Trigger.RANGED_BLOCK,
        )
        override val type: Type = Type.PERCENT
        // Smacked a PTR dummy for a bit and it was about 30%
        // Zero info online, let me know if someone has tested this better
        override fun percentChance(sp: SimParticipant): Double = 30.0

        override fun proc(
            sp: SimParticipant,
            items: List<Item>?,
            ability: Ability?,
            event: Event?
        ) {
            fireAbility.cast(sp)
        }
    }

    val buff = object : Buff() {
        override val id: Int = 28714
        override val name: String = Companion.name
        override val icon: String = Companion.icon
        override val durationMs: Int = 60000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(fireDamage = 80)
        }

        override fun procs(sp: SimParticipant): List<Proc> {
            return listOf(fireProc)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
