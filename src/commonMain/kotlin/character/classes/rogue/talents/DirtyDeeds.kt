package character.classes.rogue.talents

import character.*
import sim.SimParticipant
import data.model.Item
import sim.Event

// TODO: need to find a better way for this

class DirtyDeeds(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Dirty Deeds"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun energyReduction(): Double {
        return currentRank * 10.0
    }

    fun damageIncreaseMultiplierExecute(): Double {
        return 1.0 + (currentRank * 0.1)
    }

    val buff = character.classes.rogue.buffs.DirtyDeeds()

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}