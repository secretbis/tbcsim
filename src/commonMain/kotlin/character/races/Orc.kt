package character.races

import character.Race
import character.Ability
import character.Buff
import character.Stats
import character.races.abilities.BloodFury
import mechanics.Rating
import sim.SimParticipant

class Orc : Race() {
    override var baseStats: Stats = Stats(
        strength = 3,
        agility = -3,
        stamina = 1,
        intellect = -3,
        spirit = 2
    )

    val axeSpec = object : Buff() {
        override val name: String = "Axe Specialization"
        override val icon: String = "inv_axe_02.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val expertiseRating = 5.0 * Rating.expertiseRatingPerPoint
            return Stats(
                axeExpertiseRating = expertiseRating
            )
        }
    }

    val petDmg = object : Buff() {
        override val name: String = "Command"
        override val icon: String = "ability_warrior_warcry.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                petDamageMultiplier = 1.05
            )
        }
    }

    override fun racialByName(name: String): Ability? {
        return when(name) {
            "Blood Fury" -> BloodFury()
            else -> null
        }
    }
    override fun buffs(sp: SimParticipant): List<Buff> = listOf(axeSpec, petDmg)
}
