package character.classes.mage.talents

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class ImprovedScorch(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Scorch"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MAGE_ANY_SCORCH
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 100.0 * (currentRank / 3.0)

            val dmgBuff = object : Buff() {
                override val name: String = Companion.name
                override val durationMs: Int = 30000
                override val maxStacks: Int = 5

                override fun modifyStats(sp: SimParticipant): Stats {
                    val state = state(sp)
                    return Stats(fireDamageMultiplier = 1.0 + (state.currentStacks * 0.03))
                }
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.sim.addRaidBuff(dmgBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc>  = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
