package data.enchants

import character.Buff
import data.model.Item

object Enchants {
    fun byName(name: String, item: Item): Buff? {
        return when(name.toLowerCase().trim()) {
            "mongoose" -> Mongoose(item)
            "executioner" -> Executioner()
            else -> null
        }
    }
}
