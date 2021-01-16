package character

import data.model.Item
import data.model.default.EmptyItem

data class Gear(
    var mainHand: Item = EmptyItem(),
    var offHand: Item = EmptyItem(),
//    var twoHand: Item = EmptyItem(),
    var ranged: Item = EmptyItem(),
    var ammo: Item = EmptyItem(),

    var helm: Item = EmptyItem(),
    var neck: Item = EmptyItem(),
    var shoulders: Item = EmptyItem(),
    var back: Item = EmptyItem(),
    var chest: Item = EmptyItem(),
    var wrists: Item = EmptyItem(),
    var hands: Item = EmptyItem(),
    var waist: Item = EmptyItem(),
    var feet: Item = EmptyItem(),
    var ring1: Item = EmptyItem(),
    var ring2: Item = EmptyItem(),
    var trinket1: Item = EmptyItem(),
    var trinket2: Item = EmptyItem()
) {
    fun totalStats(): Stats {
        return Stats()
    }
}