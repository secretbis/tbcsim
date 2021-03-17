package character.classes.shaman.abilities

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class ShamanisticRage : Ability() {
    companion object {
        const val name = "Shamanistic Rage"
    }

    override val id: Int = 30823
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 120000

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
            override fun percentChance(sp: SimParticipant): Double = 35.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                val amount = (sp.attackPower() * 0.30).toInt()
                sp.addResource(amount, Resource.Type.MANA, name)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun cast(sp: SimParticipant) {
        // Apply the regen buff
        sp.addBuff(buff)
    }
}
