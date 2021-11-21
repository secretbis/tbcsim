package character.classes.hunter.abilities

import character.Ability
import character.Buff
import character.Stats
import character.classes.hunter.talents.TheBeastWithin
import sim.SimParticipant

class BestialWrath : Ability() {
    companion object {
        const val name = "Bestial Wrath"
        const val icon = "ability_druid_ferociousbite.jpg"
    }

    override val id: Int = 19574
    override val name: String = Companion.name
    override val icon: String = Companion.icon
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 120000
    override fun resourceCost(sp: SimParticipant): Double = sp.character.klass.baseMana * 0.1

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.pet != null && super.available(sp)
    }

    val petBuff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = Companion.icon
        override val durationMs: Int = 18000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalDamageMultiplier = 1.5)
        }
    }

    val playerBuff = object : Buff() {
        override val name: String = TheBeastWithin.name
        override val icon: String = Companion.icon
        override val durationMs: Int = 18000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalDamageMultiplier = 1.1)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(playerBuff)
        sp.pet?.addBuff(petBuff)
    }
}
