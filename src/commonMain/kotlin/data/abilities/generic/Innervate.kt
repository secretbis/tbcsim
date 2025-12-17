package data.abilities.generic

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class Innervate : Ability() {
    companion object {
        const val name = "Innervate"
    }

    override val id: Int = 29166
    override val name: String = Companion.name
    override val icon: String = "spell_nature_lightning.jpg"

    override fun gcdMs(sp: SimParticipant): Int = 0

    override val castableOnGcd = true

    override fun cooldownMs(sp: SimParticipant): Int = 720000

    val buff =
        object : Buff() {
            override val name: String = Companion.name
            override val icon: String = "spell_nature_lightning.jpg"
            override val durationMs: Int = 20000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(spiritRegenInCombatPct = 1.0, spiritRegenInCombatMultiplier = 4.0)
            }
        }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
