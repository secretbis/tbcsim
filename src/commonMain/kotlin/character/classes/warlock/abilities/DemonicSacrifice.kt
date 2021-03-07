package character.classes.warlock.abilities

import character.Ability
import character.Buff
import character.Stats
import sim.SimIteration

class DemonicSacrificeImp : DemonicSacrifice("Imp", { Stats(fireDamageMultiplier = 1.15)}) {
    companion object {
        const val name = "Demonic Sacrifice (Imp)"
    }
}
class DemonicSacrificeSuccubus : DemonicSacrifice("Succubus", { Stats(shadowDamageMultiplier = 1.15)}) {
    companion object {
        const val name = "Demonic Sacrifice (Succubus)"
    }
}
class DemonicSacrificeFelguard : DemonicSacrifice("Felguard", { sim -> Stats(shadowDamageMultiplier = 1.15, manaPer5Seconds = (sim.resource.maxAmount * 0.02 * 0.8).toInt())}) {
    companion object {
        const val name = "Demonic Sacrifice (Felguard)"
    }
}

open class DemonicSacrifice(suffix: String, stats: (sim: SimIteration) -> Stats) : Ability() {
    override val id: Int = 18788
    override val name: String = "Demonic Sacrifice ($suffix)"

    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    val buff = object : Buff() {
        override val name: String = "Demonic Sacrifice"
        override val durationMs: Int = 30 * 60 * 1000

        override fun modifyStats(sim: SimIteration): Stats {
            return stats(sim)
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
