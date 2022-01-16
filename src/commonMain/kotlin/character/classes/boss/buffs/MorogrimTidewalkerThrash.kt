package character.classes.boss.buffs

import character.Ability
import character.Buff
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.EventType
import sim.SimParticipant

class MorogrimTidewalkerThrash : Buff() {
    companion object {
        const val name = "Thrash (Morogrim Tidewalker)"
    }
    override val name: String = Companion.name + " (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val icon: String = "ability_ghoulfrenzy.jpg"

    val thrashAbility = object : Ability() {
        override val name: String = Companion.name
        override val icon: String = "ability_ghoulfrenzy.jpg"
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_AUTO_CRUSH,
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 50.0

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            // Immediately melee again one time
            val item = sp.character.gear.mainHand
            val damage = Melee.baseDamageRoll(sp, item)
            val result = Melee.attackRoll(sp, damage, item, isWhiteDmg = true)

            sp.logEvent(
                Event(
                    eventType = EventType.DAMAGE,
                    damageType = Constants.DamageType.PHYSICAL,
                    isWhiteDamage = true,
                    ability = thrashAbility,
                    amount = result.first,
                    result = result.second
                )
            )
        }
    }
}