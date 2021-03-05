package data.model

import character.Buff
import character.Character
import kotlinx.serialization.Serializable

@Serializable
open class ItemSet {
    @Serializable
    data class Bonus(
        val setId: Int,
        val requiredSetItems: Int,
        val buff: Buff
    ) {
        fun isActive(character: Character): Boolean {
            return character.gear.all().filter {
                it.itemSet?.id == setId
            }.size >= requiredSetItems
        }
    }

    open val id: Int = 0
    open val bonuses: List<Bonus> = listOf()
}
