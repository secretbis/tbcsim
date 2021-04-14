package character.classes.hunter.pet.buffs

import character.Buff
import character.Stats
import character.classes.hunter.talents.AnimalHandler
import mechanics.Rating
import sim.SimParticipant

class PetAnimalHandler : Buff() {
    override val name: String = "Animal Handler"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sp: SimParticipant): Stats {
        val animalHandler = sp.owner?.character?.klass?.talents?.get(AnimalHandler.name) as AnimalHandler?
        val ahHit = animalHandler?.petAdditionalHitChance() ?: 0.0
        val ahHitRating = Rating.physicalHitPerPct * ahHit

        return Stats(
            physicalHitRating = ahHitRating,
            spellHitRating = ahHitRating
        )
    }
}
