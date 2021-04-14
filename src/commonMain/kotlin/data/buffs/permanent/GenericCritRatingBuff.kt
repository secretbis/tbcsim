package data.buffs.permanent

import character.Stats

class GenericCritRatingBuff(val rating: Int) : PermanentBuff() {
    override val name: String = "Crit Rating $rating"

    override fun permanentStats(): Stats = Stats(
        meleeCritRating = rating.toDouble(),
        rangedCritRating = rating.toDouble()
    )
}
