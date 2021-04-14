package character

import data.buffs.permanent.PermanentBuff
import data.itemscustom.EmptyItem
import data.model.Color
import data.model.Item
import kotlin.js.JsExport

@JsExport
class Gear {
    // The same item can be theoretically be equipped in these slots
    var mainHand: Item = EmptyItem()
        set(value) {
            value.equippedSlot = "mainHand"
            field = value
        }
    var offHand: Item = EmptyItem()
        set(value) {
            value.equippedSlot = "offHand"
            field = value
        }

    var rangedTotemLibram: Item = EmptyItem()
    var ammo: Item = EmptyItem()
    var head: Item = EmptyItem()
    var neck: Item = EmptyItem()
    var shoulders: Item = EmptyItem()
    var back: Item = EmptyItem()
    var chest: Item = EmptyItem()
    var wrists: Item = EmptyItem()
    var hands: Item = EmptyItem()
    var waist: Item = EmptyItem()
    var legs: Item = EmptyItem()
    var feet: Item = EmptyItem()

    var ring1: Item = EmptyItem()
        set(value) {
            value.equippedSlot = "ring1"
            field = value
        }
    var ring2: Item = EmptyItem()
        set(value) {
            value.equippedSlot = "ring2"
            field = value
        }

    var trinket1: Item = EmptyItem()
        set(value) {
            value.equippedSlot = "trinket1"
            field = value
        }
    var trinket2: Item = EmptyItem()
        set(value) {
            value.equippedSlot = "trinket2"
            field = value
        }

    fun all(): List<Item> {
        return listOf(
            mainHand,
            offHand,
            rangedTotemLibram,
            ammo,
            head,
            neck,
            shoulders,
            back,
            chest,
            wrists,
            hands,
            waist,
            legs,
            feet,
            ring1,
            ring2,
            trinket1,
            trinket2
        )
    }

    fun buffs(): List<Buff> {
        val buffs = mutableListOf<Buff>()
        all().forEach {
            // Only add dynamic buffs
            buffs.addAll(it.buffs.filter { buff -> buff !is PermanentBuff })

            if(it.enchant != null) {
                buffs.add(it.enchant!!)
            }

            if(it.tempEnchant != null) {
                buffs.add(it.tempEnchant!!)
            }

            // Only meta gems provide dynamic buffs
            it.sockets.forEach { socket ->
                if(socket.color == Color.META && socket.gem != null) {
                    if(metaGemActive()) {
                        buffs.addAll(socket.gem?.buffs ?: listOf())
                    }
                }
            }
        }

        // Add any item set buffs
        val allItemSets = all().mapNotNull { it.itemSet }.distinctBy { it.id }
        allItemSets.forEach { set ->
            set.bonuses.forEach { bonus ->
                if(bonus.isActive(this)) {
                    buffs.add(bonus.buff)
                }
            }
        }

        return buffs
    }

    fun totalStats(): Stats {
        val stats = Stats()
        all().forEach {
            stats.add(it.stats)

            // Find any stats that are implemented on the server side by permanent buffs
            it.buffs.filterIsInstance<PermanentBuff>().forEach { pbuff ->
                stats.add(pbuff.permanentStats())
            }

            // Compute stats from sockets
            it.sockets.forEach { socket ->
                if(socket.gem != null) {
                    stats.add(socket.gem!!.stats)
                }
            }

            // Add socket bonuses if active
            if(it.socketBonusActive) {
                if(it.socketBonus != null) {
                    stats.add(it.socketBonus!!.stats)
                }
            }
        }
        return stats
    }

    fun metaGemActive(): Boolean {
        val allSockets = all().flatMap { it.sockets.toList() }
        val metaGem = allSockets.find { it.color == Color.META && it.gem != null }?.gem
        return metaGem?.metaActive(allSockets) ?: true
    }
}
