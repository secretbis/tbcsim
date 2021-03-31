package data.abilities.raid

import character.*
import character.classes.warlock.talents.Malediction
import sim.SimParticipant

class CurseOfTheElements : Ability() {
    companion object {
        const val name = "Curse of the Elements"
    }

    override val id: Int = 27228
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Curse of the Elements"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val malediction = sp.character.klass.talents[Malediction.name] as Malediction?
            val additionalDmgPct = (malediction?.currentRank ?: 0) * 0.01

            return Stats(
                arcaneDamageMultiplier = 1.1 + additionalDmgPct,
                fireDamageMultiplier = 1.1 + additionalDmgPct,
                frostDamageMultiplier = 1.1 + additionalDmgPct,
                shadowDamageMultiplier = 1.1 + additionalDmgPct,
            )
        }
    }

    fun debuff(owner: SimParticipant) = object : Debuff(owner) {
        override val name: String = "Curse of the Elements"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(
                arcaneResistance = -88,
                fireResistance = -88,
                frostResistance = -88,
                shadowResistance = -88,
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
        sp.sim.target.addDebuff(debuff(sp))
    }
}
