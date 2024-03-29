package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class DarkIronPipeweed : Buff() {
    companion object {
        const val name = "Dark Iron Pipeweed (static)"
    }
    override val id: Int = 51953
    override val name: String = Companion.name
    override val icon: String = "inv_misc_pipe_01.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 20000
    val buff = object : Buff() {
        override val name: String  = "Dark Iron Pipeweed"
        override val icon: String = "inv_misc_pipe_01.jpg"
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(spellDamage = 155)
        }
    }

    val ability = object : Ability() {
        override val id: Int = 35163
        override val name: String = "Dark Iron Pipeweed"
        override val icon: String = "inv_misc_pipe_01.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 120000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            sp.addBuff(buff)
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
