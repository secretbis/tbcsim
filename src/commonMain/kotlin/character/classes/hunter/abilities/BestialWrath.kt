package character.classes.hunter.abilities

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class BestialWrath : Ability() {
    companion object {
        const val name = "Bestial Wrath"
    }
    override val id: Int = 19574
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double = sp.character.klass.baseMana * 0.1

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.pet != null
    }

    val petBuff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 18000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalDamageMultiplier = 1.5)
        }
    }

    val playerBuff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 18000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalDamageMultiplier = 1.5)
        }
    }

    override fun cast(sp: SimParticipant) {
        if(sp.character.pet != null) {
            sp.pet
        }
    }
}
