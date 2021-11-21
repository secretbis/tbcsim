package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import character.classes.shaman.talents.MentalQuickness
import character.classes.shaman.talents.TotemicFocus
import character.classes.shaman.talents.TotemOfWrath as TotemOfWrathTalent
import mechanics.General
import mechanics.Rating
import sim.SimParticipant

class TotemOfWrath : Ability() {
    companion object {
        const val name = "Totem of Wrath"
    }

    override val id: Int = 30706
    override val name: String = Companion.name
    override val icon: String = "spell_fire_totemofwrath.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.totemGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double {
        val tf = sp.character.klass.talents[TotemicFocus.name] as TotemicFocus?
        val tfRed = tf?.totemCostReduction() ?: 0.0

        val mq = sp.character.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        val baseCost = 0.05 * sp.character.klass.baseMana
        return General.resourceCostReduction(baseCost, listOf(tfRed, mqRed))
    }

    override fun available(sp: SimParticipant): Boolean {
        val towTalent = sp.character.klass.talents[TotemOfWrathTalent.name] as TotemOfWrathTalent?
        return towTalent?.currentRank == 1
    }

    val buff = object : Buff() {
        override val name: String = "Totem of Wrath"
        override val icon: String = "spell_fire_totemofwrath.jpg"
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.FIRE_TOTEM)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spellCritRating = 3.0 * Rating.critPerPct,
                spellHitRating = 3.0 * Rating.spellHitPerPct
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
