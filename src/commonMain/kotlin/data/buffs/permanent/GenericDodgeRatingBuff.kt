package data.buffs.permanent

import character.Stats

class GenericDodgeRatingBuff(val rating: Int) : PermanentBuff() {
    override val name: String = "Dodge Rating $rating"

    override fun permanentStats(): Stats = Stats(dodgeRating = rating.toDouble())
}
