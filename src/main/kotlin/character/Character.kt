package character

import data.model.default.EmptyItem
import sim.Event

abstract class Character {
    // Sim and UI state
    var events: List<Event> = listOf()
    var buffs: List<Buff> = listOf()
    val stats: Stats
        get() {
            // Apply basic stats
            return Stats()
                .add(klass.baseStats)
                .add(race.baseStats)
                .add(gear.totalStats())
                .apply {
                    // Apply flat modifiers from buffs
                    buffs.filter { it.statModType == Buff.ModType.FLAT }.forEach {
                        it.modifyStats(this)
                    }
                }
                .apply {
                    // Apply percentage modifiers from buffs
                    buffs.filter { it.statModType == Buff.ModType.PERCENTAGE }.forEach {
                        it.modifyStats(this)
                    }
                }
        }
    var gear: Gear = Gear()

    // Setup data
    abstract var klass: Class
    abstract var race: Race


    abstract var resource: Resource
    abstract var baseResourceAmount: Int

    abstract var canDualWield: Boolean
    fun isDualWielding(): Boolean {
        return !(gear.mainHand is EmptyItem || gear.offHand is EmptyItem)
    }
}
