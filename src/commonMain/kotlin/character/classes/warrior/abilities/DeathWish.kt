package character.classes.warrior.abilities

import character.Ability
import character.Buff
import character.Resource
import character.Stats
import character.classes.warrior.talents.DeathWish as DeathWishTalent
import sim.SimIteration

class DeathWish : Ability() {
    companion object {
        const val name = "Death Wish"
    }

    override val id: Int = 12292
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()
    override fun cooldownMs(sim: SimIteration): Int = 180000

    override fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sim: SimIteration): Double = 10.0

    override fun available(sim: SimIteration): Boolean {
        return sim.subject.klass.talents[DeathWishTalent.name]?.currentRank == 1 && super.available(sim)
    }

    val buff = object : Buff() {
        override val name: String = "Death Wish"
        override val durationMs: Int = 30000

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(physicalDamageMultiplier = 1.2)
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
