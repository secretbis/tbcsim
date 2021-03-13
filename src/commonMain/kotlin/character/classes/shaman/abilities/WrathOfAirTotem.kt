package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import character.classes.shaman.talents.MentalQuickness
import character.classes.shaman.talents.TotemicFocus
import mechanics.General
import sim.SimParticipant

class WrathOfAirTotem : Ability() {
    companion object {
        const val name = "Wrath of Air Totem"
    }

    override val id: Int = 3738
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.totemGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double {
        val tf = sp.character.klass.talents[TotemicFocus.name] as TotemicFocus?
        val tfRed = tf?.totemCostReduction() ?: 0.0

        val mq = sp.character.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        val baseCost = 320.0
        return General.resourceCostReduction(baseCost, listOf(tfRed, mqRed))
    }

    override fun available(sp: SimParticipant): Boolean {
        return true
    }

    val buff = object : Buff() {
        override val name: String = "Wrath of Air Totem"
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.AIR_TOTEM)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellDamage = 101
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
