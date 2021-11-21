package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import character.classes.shaman.talents.EnhancingTotems
import character.classes.shaman.talents.MentalQuickness
import character.classes.shaman.talents.TotemicFocus
import mechanics.General
import sim.SimParticipant

class GraceOfAirTotem : Ability() {
    companion object {
        const val name = "Grace of Air Totem"
    }

    override val id: Int = 25359
    override val name: String = Companion.name
    override val icon: String = "spell_nature_invisibilitytotem.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.totemGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double {
        val tf = sp.character.klass.talents[TotemicFocus.name] as TotemicFocus?
        val tfRed = tf?.totemCostReduction() ?: 0.0

        val mq = sp.character.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        return General.resourceCostReduction(310.0, listOf(tfRed, mqRed))
    }

    override fun available(sp: SimParticipant): Boolean {
        return true
    }

    val buff = object : Buff() {
        override val name: String = "Grace of Air Totem"
        override val icon: String = "spell_nature_invisibilitytotem.jpg"
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.AIR_TOTEM)

        val baseAgi = 77.0
        override fun modifyStats(sp: SimParticipant): Stats {
            val etTalent = sp.character.klass.talents[EnhancingTotems.name] as EnhancingTotems?
            val multiplier = 1.0 * (etTalent?.graceOfAirTotemMultiplier() ?: 1.0)
            return Stats(agility = (baseAgi * multiplier).toInt())
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
