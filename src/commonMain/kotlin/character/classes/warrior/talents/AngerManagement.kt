package character.classes.warrior.talents

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class AngerManagement(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Anger Management"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1

    val buff = object : Buff() {
        override val name: String = "Anger Management"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SERVER_SLOW_TICK
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(1, Resource.Type.RAGE, name)
            }
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
