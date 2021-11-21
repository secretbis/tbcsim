package character.classes.rogue.talents

import character.*
import sim.SimParticipant
import data.model.Item
import sim.Event

class Ruthlessness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Ruthlessness"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val ruthlessnessAbility = object : Ability() {
        override val name: String = Companion.name
        override val icon: String = "ability_druid_disembowel.jpg"
    }

    fun chanceToAddComboPointPercent(): Double {
        return currentRank * 20.0
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val icon: String = "ability_druid_disembowel.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.ROGUE_CAST_FINISHER
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = chanceToAddComboPointPercent()

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(1, Resource.Type.COMBO_POINT, ruthlessnessAbility)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
