package data.buffs

import character.Buff

object Buffs {
    fun byId(id: Int): Buff? {
        return allBuffs.find { it.id == id }
    }

    // This should omit generic buff templates
    // Every item in this list must have a distinct class name
    val allBuffs = listOf(
        DragonspineTrophy(),
    )
}
