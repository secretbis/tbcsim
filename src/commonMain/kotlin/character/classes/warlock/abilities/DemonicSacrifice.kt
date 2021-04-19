package character.classes.warlock.abilities

import character.Ability
import character.Buff
import character.Stats
import character.Resource
import sim.SimIteration
import sim.SimParticipant

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
class DemonicSacrificeFelguard : DemonicSacrifice("Felguard", { sp -> Stats(shadowDamageMultiplier = 1.15, manaPer5Seconds = (sp.getResource((Resource.Type.MANA)).maxAmount * 0.02 * 0.8).toInt())}) {
    companion object {
        const val name = "Demonic Sacrifice (Felguard)"
    }
}

open class DemonicSacrifice(suffix: String, stats: (sp: SimParticipant) -> Stats) : Ability() {
    override val id: Int = 18788
    override val name: String = "Demonic Sacrifice ($suffix)"

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.klass.talents[character.classes.warlock.talents.DemonicSacrifice.name]?.currentRank ?: 0  > 0
    }

    val buff = object : Buff() {
        override val name: String = "Demonic Sacrifice"
        override val durationMs: Int = 30 * 60 * 1000

        override fun modifyStats(sp: SimParticipant): Stats {
            return stats(sp)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
