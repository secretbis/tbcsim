package character.classes.warrior.talents

import character.Ability
import character.Buff
import character.Proc
import character.Talent
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

class SwordSpec(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Sword Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
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
            override val percentChance: Double = 5.0 * currentRank

            private fun isSword(item: Item): Boolean {
                return item.itemSubclass == Constants.ItemSubclass.SWORD_2H ||
                       item.itemSubclass == Constants.ItemSubclass.SWORD_1H
            }

            override fun shouldProc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
                // Sword spec cannot proc off itself
                val isSwordSpec = ability?.name == Companion.name
                return !isSwordSpec && items?.all { isSword(it) } ?: false && super.shouldProc(sim, items, ability, event)
            }

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                val item = items?.get(0)
                if(item == null || !isSword(item)) {
                    logger.warn { "Tried to proc warrior Sword Specialization, but the context was not a sword." }
                    return
                }

                val isOffhand = item === sim.subject.gear.offHand
                val damageRoll = Melee.baseDamageRoll(sim, item)
                val result = Melee.attackRoll(sim, damageRoll, true, isOffhand)

                sim.logEvent(Event(
                    eventType = Event.Type.DAMAGE,
                    damageType = Constants.DamageType.PHYSICAL,
                    abilityName = name,
                    amount = result.first,
                    result = result.second,
                ))
            }
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
