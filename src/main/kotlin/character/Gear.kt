package character

import data.model.Item

data class Gear(
    var mainHand: Item = Item(),
    var offHand: Item = Item(),
//    var twoHand: Item = Item(),
    var ranged: Item = Item(),
    var ammo: Item = Item(),

    var helm: Item = Item(),
    var neck: Item = Item(),
    var shoulders: Item = Item(),
    var back: Item = Item(),
    var chest: Item = Item(),
    var wrists: Item = Item(),
    var hands: Item = Item(),
    var waist: Item = Item(),
    var feet: Item = Item(),
    var ring1: Item = Item(),
    var ring2: Item = Item(),
    var trinket1: Item = Item(),
    var trinket2: Item = Item()
) {
    fun procs(): List<Proc> {
        // TODO: Extract procs from equipped gear
        return listOf()
    }

    fun totalStats(): Stats {
        // TODO: Sum of stats from equipped gear
        return Stats()
    }
}
