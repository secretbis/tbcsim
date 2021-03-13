package character.races

import character.Ability
import character.Buff
import character.Race
import character.Stats
import sim.SimParticipant

class Undead : Race() {
    override var baseStats: Stats = Stats(
        strength = -1,
        agility = -2,
        stamina = 0,
        intellect = -2,
        spirit = 5
    )

    override fun racialByName(name: String): Ability? = null
    override fun buffs(sp: SimParticipant): List<Buff> = listOf()
}
