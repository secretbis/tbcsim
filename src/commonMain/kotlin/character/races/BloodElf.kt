package character.races

import character.Ability
import character.Buff
import character.Race
import character.Stats
import sim.SimParticipant

class BloodElf : Race() {
    override var baseStats: Stats = Stats(
        strength = -3,
        agility = 2,
        stamina = 0,
        intellect = 3,
        spirit = -2
    )

    override fun racialByName(name: String): Ability? = null
    override fun buffs(sp: SimParticipant): List<Buff> = listOf()
}
