package character.classes.shaman.talents

import character.Ability
import character.Proc
import character.Talent
import data.model.Item
import sim.SimIteration
import character.classes.shaman.buffs.UnleashedRage as UnleashedRageBuff

class UnleashedRage : Talent() {
    companion object {
        const val name: String = "Unleashed Rage"
    }

    override val name: String = Companion.name
    override var maxRank: Int = 5
    override var currentRank: Int = 0

    val buff = UnleashedRageBuff()

    override val procs: List<Proc> = listOf(
        object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_CRIT
            )

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                sim.addBuff(buff)
            }
        }
    )
}
