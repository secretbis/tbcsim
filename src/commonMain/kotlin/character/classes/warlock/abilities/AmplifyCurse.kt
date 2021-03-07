package character.classes.warlock.abilities

import character.Ability
import character.Buff
import character.Resource
import character.classes.warlock.talents.AmplifyCurse
import sim.SimIteration

class AmplifyCurse : Ability() {
    companion object {
        const val name = "Amplify Curse"
    }
    override val id: Int = 18288
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()
    override fun cooldownMs(sim: SimIteration): Int = 180000

    override fun available(sim: SimIteration): Boolean {
        return (sim.subject.klass.talents[AmplifyCurse.name]?.currentRank ?: 0) > 0
    }

    override fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.MANA
    override fun resourceCost(sim: SimIteration): Double = 0.0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 30000
        override val hidden: Boolean = true
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
