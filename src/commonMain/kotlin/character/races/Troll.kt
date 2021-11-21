package character.races

import character.Race
import character.Ability
import character.Buff
import character.Stats
import character.CharacterType
import character.races.abilities.Berserking
import mechanics.Rating
import sim.SimParticipant

class Troll : Race() {
    override var baseStats: Stats = Stats(
        strength = 1,
        agility = 2,
        intellect = -4,
        spirit = 1
    )

    override fun racialByName(name: String): Ability? {
        return when(name) {
            "Berserking" -> Berserking()
            else -> null
        }
    }

    val beastSlaying = object : Buff() {
        override val name: String = "Beast Slaying"
        override val icon: String = "inv_misc_pelt_bear_ruin_02.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats? {
            return if(sp.sim.target.character.subTypes.intersect(setOf(CharacterType.BEAST)).isNotEmpty()) {
                Stats(
                    physicalDamageMultiplier = 1.05,
                )
            } else null
        }
    }

    val bowSpec = object : Buff() {
        override val name: String = "Bow Specialization"
        override val icon: String = "inv_weapon_bow_12.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(bowCritRating = 1.0 * Rating.critPerPct)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(beastSlaying, bowSpec)
}
