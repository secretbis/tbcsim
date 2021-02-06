package data.buffs.permanent

import character.Stats

class GenericSpellHitRatingBuff(val rating: Int) : PermanentBuff() {
    override val name: String = "Spell Hit Rating $rating"

    override fun permanentStats(): Stats = Stats(spellHitRating = rating.toDouble())
}
