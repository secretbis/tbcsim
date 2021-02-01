package character.races

import character.Race
import character.Ability
import character.Buff
import character.Stats
import character.classes.mage.Mage
import character.classes.priest.Priest
import character.classes.shaman.Shaman
import mechanics.Rating
import sim.SimIteration

class Draenei : Race() {
    override var baseStats: Stats = Stats(
        strength = 1,
        agility = -3,
        spirit = 2
    )
    override fun racialByName(name: String): Ability? = null

    val heroicPresence = object : Buff() {
        override val name: String = "Heroic Presence"
        override val durationMs: Int = -1

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(physicalHitRating = 1.0 * Rating.meleeHitPerPct)
        }
    }

    val inspiringPresence = object : Buff() {
        override val name: String = "Inspiring Presence"
        override val durationMs: Int = -1

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(spellHitRating = 1.0 * Rating.spellHitPerPct)
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> {
        val k = sim.subject.klass
        return if(k is Shaman || k is Mage || k is Priest) {
            listOf(inspiringPresence)
        } else {
            listOf(heroicPresence)
        }
    }
}
