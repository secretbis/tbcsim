package data.buffs.permanent

import character.Stats

class GenericHitRatingBuff(val rating: Int) : PermanentBuff() {
    override val name: String = "Hit Rating $rating"

    override fun permanentStats(): Stats = Stats(physicalHitRating = rating.toDouble())
}
