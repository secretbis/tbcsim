package data.buffs

import character.Ability
import character.Buff
import character.Stats
import sim.SimParticipant

class FelInfusion : Buff() {
    companion object {
        const val name = "Fel Infusion"
    }
    override val id: Int = 40396
    override val name: String = Companion.name + " (static)"
    override val icon: String = "spell_shadow_metamorphosis.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 20000
    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_shadow_metamorphosis.jpg"
        override val durationMs: Int = buffDurationMs

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(spellHasteRating = 175.0)
        }
    }

    val ability = object : Ability() {
        override val id: Int = 40396
        override val name: String = Companion.name
        override val icon: String = "spell_shadow_metamorphosis.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 120000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            sp.addBuff(buff)
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
