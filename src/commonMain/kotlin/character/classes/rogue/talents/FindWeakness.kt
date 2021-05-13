package character.classes.rogue.talents

import character.*
import sim.SimParticipant
import data.model.Item
import sim.Event
import kotlin.random.Random

class FindWeakness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Find Weakness"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun damageMultiplier(): Double {
        return 1.0 + (currentRank * 0.02)
    }

    val dmgBuff = object : Buff() {
        override val name: String = "Find Weakness"
        override val durationMs: Int = 10000

        override fun modifyStats(sp: SimParticipant): Stats {  
            // unsure if yellowDamageMultiplier or yellowDamageFlatModifier
            return Stats(
                yellowDamageMultiplier = damageMultiplier()
            )
        }
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.ROGUE_CAST_FINISHER
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(dmgBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}