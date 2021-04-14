package data.abilities.raid

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import sim.SimParticipant

class ExposeWeakness(val agility: Int): Ability() {
    override val id: Int = 34501
    override val name: String = "Expose Weakness ($agility Agi)"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Expose Weakness ($agility Agi)"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val mutex: List<Mutex> = listOf(Mutex.BUFF_EXPOSE_WEAKNESS)
        override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
            return mapOf(
                Mutex.BUFF_EXPOSE_WEAKNESS to agility
            )
        }

        // Assume a GoA uptime of about 80% when twisting
        // Also assume the caster has Enhancing Totems
        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                attackPower = (0.25 * agility).toInt(),
                rangedAttackPower = (0.25 * agility).toInt()
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
