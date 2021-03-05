package character.classes.shaman.abilities

import character.*
import data.model.Item
import sim.Event
import sim.SimIteration

class ShamanisticRage : Ability() {
    companion object {
        const val name = "Shamanistic Rage"
    }

    override val id: Int = 30823
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()
    override fun cooldownMs(sim: SimIteration): Int = 120000

    val buff = object : Buff() {
        override val name: String = "Shamanistic Rage"
        override val durationMs: Int = 15000

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_BLOCK,
                Trigger.MELEE_GLANCE,
            )
            override val type: Type = Type.PERCENT
            // TODO: Shamanistic Rage proc chance unconfirmed
            override fun percentChance(sim: SimIteration): Double = 35.0

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                val amount = (sim.attackPower() * 0.30).toInt()
                sim.addResource(amount, Resource.Type.MANA)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override fun cast(sim: SimIteration) {
        // Apply the regen buff
        sim.addBuff(buff)
    }
}
