package character.classes.hunter.pet.buffs

import character.Buff
import character.Stats
import character.classes.hunter.talents.Ferocity
import mechanics.Rating
import sim.SimParticipant

class PetFerocity: Buff() {
    override val name: String = "Ferocity"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sp: SimParticipant): Stats {
        val ferocity = sp.owner?.character?.klass?.talents?.get(Ferocity.name) as Ferocity?
        val ferocityCrit = ferocity?.petAdditionalCritPct() ?: 0.0
        val ferocityCritRating = Rating.critPerPct * ferocityCrit

        return Stats(
            meleeCritRating = ferocityCritRating,
            rangedCritRating = ferocityCritRating,
            spellCritRating = ferocityCritRating
        )
    }
}
