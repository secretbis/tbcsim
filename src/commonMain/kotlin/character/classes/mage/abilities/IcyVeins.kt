package character.classes.mage.abilities

import character.Ability
import character.Buff
import character.Stats
import character.classes.mage.talents.FrostChanneling
import character.classes.mage.talents.IceFloes
import mechanics.Rating
import sim.SimParticipant

class IcyVeins : Ability() {
    companion object {
        const val name: String = "Icy Veins"
    }
    override val id: Int = 12472
    override val name: String = Companion.name
    override val icon: String = "spell_frost_coldhearted.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun castTimeMs(sp: SimParticipant): Int = 0

    val baseCooldownMs = 180000
    override fun cooldownMs(sp: SimParticipant): Int {
        val iceFloesMult = 1.0 - (sp.character.klass.talentRanks(IceFloes.name) * 0.1)
        return (baseCooldownMs * iceFloesMult).toInt()
    }

    override fun resourceCost(sp: SimParticipant): Double {
        val frostChanneling: FrostChanneling? = sp.character.klass.talentInstance(FrostChanneling.name)
        val fcMult = frostChanneling?.frostSpellManaCostReductionMultiplier() ?: 1.0

        return sp.character.klass.baseMana * 0.03 * fcMult
    }

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_frost_coldhearted.jpg"
        override val durationMs: Int = 20000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellHasteRating = 20.0 * Rating.hastePerPct)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
