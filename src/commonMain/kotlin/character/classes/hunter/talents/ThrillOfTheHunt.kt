package character.classes.hunter.talents

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class ThrillOfTheHunt(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Thrill of the Hunt"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val tothAbility = object : Ability() {
        override val name: String = Companion.name
        override val icon: String = "ability_hunter_thrillofthehunt.jpg"
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val icon: String = "ability_hunter_thrillofthehunt.jpg"

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.RANGED_AUTO_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 100.0 * (currentRank / 3.0)

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                if(ability != null) {
                    val refund = (ability.resourceCost(sp) * 0.4).toInt()
                    sp.addResource(refund, Resource.Type.MANA, tothAbility)
                }
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
