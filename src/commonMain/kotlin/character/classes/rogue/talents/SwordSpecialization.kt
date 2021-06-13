package character.classes.rogue.talents

import character.Ability
import character.Buff
import character.Proc
import character.Talent
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.EventType
import sim.SimParticipant

class SwordSpecialization(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Sword Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun additionalAttackChancePercent(): Double {
        return currentRank * 1.0
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_GLANCE,
                Trigger.MELEE_BLOCK
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = additionalAttackChancePercent()

            override fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
                // Sword spec cannot proc off itself
                val isSwordSpec = ability?.name == name
                return !isSwordSpec && items?.all { Melee.isSword(it) } ?: false && super.shouldProc(sp, items, ability, event)
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                val item = items?.get(0)
                if(item == null || !Melee.isSword(item)) {
                    logger.warn { "Tried to proc rogue Sword Specialization, but the context was not a sword." }
                    return
                }

                // the additional attack is always carried out by the mainhand
                val attackItem = sp.character.gear.mainHand
                val damageRoll = Melee.baseDamageRoll(sp, attackItem)
                val result = Melee.attackRoll(sp, damageRoll, attackItem, isWhiteDmg = true)

                sp.logEvent(Event(
                    eventType = EventType.DAMAGE,
                    damageType = Constants.DamageType.PHYSICAL,
                    abilityName = name,
                    amount = result.first,
                    result = result.second,
                ))
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
