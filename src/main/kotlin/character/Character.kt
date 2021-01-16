package character

import sim.Event

abstract class Character {
    // Sim and UI state
    var events: MutableList<Event> = mutableListOf()
    var buffs: MutableList<Buff> = mutableListOf()
    lateinit var stats: Stats
    var gear: Gear = Gear()

    // Setup data
    abstract var klass: Class
    abstract var race: Race

    abstract var resource: Resource
    abstract var baseResourceAmount: Int

    abstract var canDualWield: Boolean

    init {
        computeStats()
    }

    fun isDualWielding(): Boolean {
        return gear.mainHand.id != -1 && gear.offHand.id != -1
    }

    fun computeStats() {
        // Apply basic stats
        this.stats = Stats()
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
}
