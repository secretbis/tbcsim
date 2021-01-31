package character.classes.shaman.abilities

import character.*
import data.model.Item
import sim.SimIteration

class ShamanisticRage : Ability() {
    companion object {
        const val name = "Shamanistic Rage"
    }

    override val id: Int = 30823
    override val name: String = Companion.name
    override fun cooldownMs(sim: SimIteration): Int = 120000

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1

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
            override val percentChance: Double = 0.3

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                val amount = (sim.attackPower() * 0.30).toInt()
                sim.addResource(amount, Resource.Type.MANA)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        // Apply the regen buff
        sim.addBuff(buff)
    }

    override val baseCastTimeMs: Int = 0
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()
}
