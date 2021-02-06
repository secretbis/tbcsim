package data.buffs.permanent

import character.Stats

class GenericBlockValueBuff(val blockValue: Int) : PermanentBuff() {
    override val name: String = "Block Value $blockValue"

    override fun permanentStats(): Stats = Stats(blockValue = blockValue.toDouble())
}
