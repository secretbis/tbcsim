package character.classes.priest.abilities

import character.Ability
import character.Buff
import sim.SimParticipant

class Shadowfiend : Ability() {
    companion object {
        const val name: String = "Shadowfiend"
    }
    override val id: Int = 34433
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun castTimeMs(sp: SimParticipant): Int  = 0

    override fun cooldownMs(sp: SimParticipant): Int = 300000

    override fun resourceCost(sp: SimParticipant): Double {
        return sp.character.klass.baseMana * 0.06
    }

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 15000
        override val hidden: Boolean = true

        override fun reset(sp: SimParticipant) {
            sp.pet?.deactivate(true)
            super.reset(sp)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.pet?.activate()
        sp.addBuff(buff)
    }
}
