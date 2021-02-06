package data.buffs.permanent

import character.Stats

class GenericManaRegenBuff(val regen: Int): PermanentBuff() {
    override val name: String = "Mana Regen $regen/5s"

    override fun permanentStats(): Stats = Stats(manaPer5Seconds = regen)
}
