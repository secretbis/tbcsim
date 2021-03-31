package character.classes.hunter.abilities

import character.Ability
import character.Buff
import character.Proc
import data.model.Item
import sim.Event
import sim.SimParticipant

class KillCommand : Ability() {
    companion object {
        const val name = "Kill Command"
        const val flagBuffName = "Kill Command (available)"

        // This is just a marker to tell the rotation that we can cast Kill Command
        val killCommandFlagBuff = object : Buff() {
            override val name: String = flagBuffName
            override val durationMs: Int = 3000 // TODO: 1 server tick?  Needs confirmation
            override val hidden: Boolean = true
        }
    }

    override val id: Int = 34026
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 5000
    override fun resourceCost(sp: SimParticipant): Double = 75.0

    override fun available(sp: SimParticipant): Boolean {
        val hasTriggerBuff = sp.buffs[flagBuffName] != null
        return hasTriggerBuff && super.available(sp)
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (base)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // On crit, proc the buff that lets us use Kill Command
        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.RANGED_AUTO_CRIT,
                Trigger.RANGED_WHITE_CRIT,
                Trigger.RANGED_YELLOW_CRIT
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(killCommandFlagBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
    }
}
