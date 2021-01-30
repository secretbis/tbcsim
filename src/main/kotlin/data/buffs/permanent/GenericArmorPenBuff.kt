package data.buffs.permanent

import character.Stats

class GenericArmorPenBuff(val armorPen: Int): PermanentBuff() {
    override val name: String = "Armor Penetration $armorPen"

    override fun permanentStats(): Stats = Stats(armorPen = armorPen)
}
