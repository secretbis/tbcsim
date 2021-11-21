package character.races

import character.Ability
import character.Buff
import character.Race
import character.Stats
import character.races.abilities.Starshards
import mechanics.Rating
import sim.SimParticipant

class NightElf : Race() {
    override var baseStats: Stats = Stats(
        strength = -4,
        agility = 4,
        stamina = 0,
        intellect = 0,
        spirit = 0
    )

    val dodge = object : Buff() {
        override val name: String = "Quickness"
        override val icon: String = "ability_racial_shadowmeld.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                dodgeRating = 1.0 * Rating.dodgePerPct
            )
        }
    }

    override fun racialByName(name: String): Ability? {
        return when(name) {
            "Starshards" -> Starshards()
            else -> null
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(dodge)
}
