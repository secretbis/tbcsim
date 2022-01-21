package character.classes.warrior.talents

import character.*
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimParticipant

class MaceSpec(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mace Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val msAbility = object : Ability() {
        override val name: String = Companion.name
        override val icon: String = "inv_mace_01.jpg"
    }

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_mace_01.jpg"
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
            override fun percentChance(sp: SimParticipant): Double = 5.0 * currentRank

            override fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
                return items?.all { it.isMace() } ?: false && super.shouldProc(sp, items, ability, event)
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                val item = items?.get(0)
                if(item == null || !item.isMace()) {
                    logger.warn { "Tried to proc warrior Mace Specialization, but the context was not a mace." }
                    return
                }

                sp.addResource(7, Resource.Type.RAGE, msAbility)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
