package character.classes.warlock.abilities

import character.Ability
import character.Buff
import character.Resource
import character.Stats
import character.classes.warlock.talents.DemonicAegis
import sim.SimIteration

class FelArmor : Ability() {
    companion object {
        const val name = "Fel Armor"
    }
    override val id: Int = 28189
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.MANA
    override fun resourceCost(sim: SimIteration): Double = 725.0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 30 * 60 * 1000

        override fun modifyStats(sim: SimIteration): Stats {
            val demonicAegis = sim.subject.klass.talents[DemonicAegis.name] as DemonicAegis?
            val spellDmg = (100.0 * (demonicAegis?.improvedArmorMultiplier() ?: 1.0)).toInt()

            return Stats(
                frostDamage = spellDmg,
                shadowDamage = spellDmg,
                arcaneDamage = spellDmg,
                fireDamage = spellDmg,
                natureDamage = spellDmg,
                holyDamage = spellDmg
            )
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
