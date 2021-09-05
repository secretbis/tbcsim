package character.races

import character.Ability
import character.Buff
import character.Race
import character.Stats
import character.races.abilities.DevouringPlague
import sim.SimParticipant

class Undead : Race() {
    override var baseStats: Stats = Stats(
        strength = -1,
        agility = -2,
        stamina = 0,
        intellect = -2,
        spirit = 5
    )

    override fun racialByName(name: String): Ability? {
        return when(name) {
            "Devouring Plague" -> DevouringPlague()
            else -> null
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf()
}
