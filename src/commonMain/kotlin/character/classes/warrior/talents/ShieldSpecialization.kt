package character.classes.warrior.talents

import character.*
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class ShieldSpecialization(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shield Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_shield_06.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val procAbility = object : Ability() {
            override val name: String = Companion.name
            override val icon: String = "inv_shield_06.jpg"
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_BLOCK
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double {
                return 20.0 * currentRank
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(1, Resource.Type.RAGE, procAbility)
            }
        }

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                blockRating = Rating.blockPerPct * currentRank
            )
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
