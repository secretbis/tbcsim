package character.classes.mage.abilities

import character.Ability
import character.Buff
import character.classes.mage.talents.FrostChanneling
import sim.SimParticipant

class SummonWaterElemental : Ability() {
    companion object {
        const val name: String = "Summon Water Elemental"
    }
    override val id: Int = 30451
    override val name: String = Companion.name
    override val icon: String = "spell_frost_summonwaterelemental_2.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun castTimeMs(sp: SimParticipant): Int  = 0

    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun resourceCost(sp: SimParticipant): Double {
        val frostChanneling: FrostChanneling? = sp.character.klass.talentInstance(FrostChanneling.name)
        val fcMult = frostChanneling?.frostSpellManaCostReductionMultiplier() ?: 1.0

        return sp.character.klass.baseMana * 0.16 * fcMult
    }

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_frost_summonwaterelemental_2.jpg"
        override val durationMs: Int = 45000
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
