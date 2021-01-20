package character

import data.model.Item
import kotlin.reflect.full.declaredMemberProperties

data class Gear(
    var mainHand: Item = Item(),
    var offHand: Item = Item(),
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
    fun buffs(): List<Buff> {
        val buffs = mutableListOf<Buff>()
        Gear::class.declaredMemberProperties.forEach {
            val enchant = (it.get(this) as Item).enchant
            val tempEnh = (it.get(this) as Item).temporaryEnhancement

            if(enchant != null) {
                buffs.add(enchant)
            }

            if(tempEnh != null) {
                buffs.add(tempEnh)
            }
        }
        return buffs
    }

    fun procs(): List<Proc> {
        val procs = mutableListOf<Proc>()
        Gear::class.declaredMemberProperties.forEach {
            procs.addAll((it.get(this) as Item).procs)
        }
        return procs
    }

    fun totalStats(): Stats {
        val stats = Stats()
        Gear::class.declaredMemberProperties.forEach {
            stats.add((it.get(this) as Item).stats)
        }
        return stats
    }
}
