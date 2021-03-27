package character.classes.shaman.talents

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class UnleashedRage(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Unleashed Rage"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = "Unleashed Rage (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_CRIT
            )
            override val type: Type = Type.STATIC

            val buff = object : Buff() {
                override val name: String = "Unleashed Rage"
                override val durationMs: Int = 10000

                override fun modifyStats(sp: SimParticipant): Stats {
                    val talentRanks = sp.character.klass.talents[UnleashedRage.name]?.currentRank ?: 0

                    val modifier = 1.0 + (0.2 * talentRanks)
                    return Stats(
                        attackPowerMultiplier = modifier,
                        rangedAttackPowerMultiplier = modifier
                    )
                }
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(buff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
