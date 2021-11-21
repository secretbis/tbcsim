package character.classes.hunter.pet.buffs

import character.Buff
import character.Stats
import character.classes.hunter.talents.SerpentsSwiftness
import mechanics.Rating
import sim.SimParticipant

class PetSerpentsSwiftness : Buff() {
    override val name: String = "Serpent's Swiftness (Pet)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val icon: String = "ability_hunter_serpentswiftness.jpg"

    override fun modifyStats(sp: SimParticipant): Stats {
        val serpentsSwiftness = sp.owner?.character?.klass?.talents?.get(SerpentsSwiftness.name) as SerpentsSwiftness?
        val ssPetHastePct = serpentsSwiftness?.petHastePct() ?: 0.0
        val ssPetHasteRating = ssPetHastePct * Rating.hastePerPct

        return Stats(
            physicalHasteRating = ssPetHasteRating
        )
    }
}
