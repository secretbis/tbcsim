package character.races

import character.Ability
import character.Buff
import character.Race
import character.Stats
import sim.SimParticipant

class Boss : Race() {
    override var baseStats: Stats = Stats()
    override fun racialByName(name: String): Ability? = null
    override fun buffs(sp: SimParticipant): List<Buff> = listOf()
}
