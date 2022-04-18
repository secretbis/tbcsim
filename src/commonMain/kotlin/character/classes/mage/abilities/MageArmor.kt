package character.classes.mage.abilities

import character.Ability
import character.Buff
import character.Stats
import mechanics.General
import sim.SimParticipant

class MageArmor : Ability() {
    companion object {
        const val name = "Mage Armor"
    }
    override val id: Int = 22783
    override val name: String = Companion.name
    override val icon: String = "spell_magearmor.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun resourceCost(sp: SimParticipant): Double = 630.0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_magearmor.jpg"
        override val durationMs: Int = 30 * 60 * 1000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(manaPer5Seconds = (General.mp5FromSpiritNotCasting(sp) * .3).toInt())
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}