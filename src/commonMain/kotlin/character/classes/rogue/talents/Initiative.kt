package character.classes.rogue.talents

import character.*
import sim.SimParticipant
import data.model.Item
import sim.Event
import character.classes.rogue.abilities.*

class Initiative(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Initiative"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun chanceToAddComboPointPercent(): Double {
        return currentRank * 25.0
    }

    val cpProducingAbilities: Set<String> = setOf(
        Ambush.name,
        Garrote.name
    )

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_YELLOW_HIT,
            )

            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = chanceToAddComboPointPercent()

            override fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
                return (cpProducingAbilities.contains(ability?.name)) && super.shouldProc(sp, items, ability, event)
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(1, Resource.Type.COMBO_POINT, name)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}