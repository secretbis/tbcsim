package character.classes.warlock.abilities

import character.Ability
import character.Buff
import character.Resource
import character.Stats
import character.classes.warlock.talents.DemonicAegis
import sim.SimParticipant

class FelArmor : Ability() {
    companion object {
        const val name = "Fel Armor"
    }
    override val id: Int = 28189
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.MANA
    override fun resourceCost(sp: SimParticipant): Double = 725.0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 30 * 60 * 1000

        override fun modifyStats(sp: SimParticipant): Stats {
            val demonicAegis = sp.character.klass.talents[DemonicAegis.name] as DemonicAegis?
            val spellDmg = (100.0 * (demonicAegis?.improvedArmorMultiplier() ?: 1.0)).toInt()

            return Stats(spellDamage = spellDmg)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
