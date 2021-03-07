package data.abilities.raid

import character.*
import character.classes.warlock.talents.Malediction
import sim.SimIteration

class CurseOfTheElements : Ability() {
    companion object {
        const val name = "Curse of the Elements"
    }

    override val id: Int = 27228
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = "Curse of the Elements"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            val malediction = sim.subject.klass.talents[Malediction.name] as Malediction?
            val additionalDmgPct = (malediction?.currentRank ?: 0) * 0.01

            return Stats(
                arcaneDamageMultiplier = 1.1 + additionalDmgPct,
                fireDamageMultiplier = 1.1 + additionalDmgPct,
                frostDamageMultiplier = 1.1 + additionalDmgPct,
                shadowDamageMultiplier = 1.1 + additionalDmgPct,
            )
        }
    }

    val debuff = object : Debuff() {
        override val name: String = "Curse of the Elements"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats? {
            return Stats(
                arcaneResistance = -88,
                fireResistance = -88,
                frostResistance = -88,
                shadowResistance = -88,
            )
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
        sim.addDebuff(debuff)
    }
}
