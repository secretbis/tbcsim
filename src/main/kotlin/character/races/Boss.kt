package character.races

import character.Ability
import character.Race
import character.Stats

class Boss : Race() {
    override var baseStats: Stats = Stats()
    override var racials: List<Ability> = listOf()
}
