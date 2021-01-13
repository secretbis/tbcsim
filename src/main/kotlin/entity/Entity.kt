package entity

import sim.Event

interface Entity {
    var spells: List<Spell>
    var buffs: List<Buff>
    var stats: Stats
    var race: Race
    var talents: List<Talent>

    var resourceType: Resource.Type
    var resource: Resource
    var baseResourceAmount: Int

    var canDualWield: Boolean
    var isDualWielding: Boolean

    // Stats tracking
    var events: List<Event>
}
