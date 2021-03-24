package data.model

import character.Buff
import character.Character
import character.Gear

open class ItemSet {
    data class Bonus(
        val setId: Int,
        val requiredSetItems: Int,
        val buff: Buff
    ) {
        fun isActive(gear: Gear): Boolean {
            return gear.all().filter {
                it.itemSet?.id == setId
            }.size >= requiredSetItems
        }
    }

    open val id: Int = 0
    open val bonuses: List<Bonus> = listOf()
}
