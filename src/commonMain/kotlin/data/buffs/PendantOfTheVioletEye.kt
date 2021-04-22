package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class PendantOfTheVioletEye : Buff() {
    companion object {
        const val name = "Pendant of the Violet Eye (static)"
    }
    override val id: Int = 29601
    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val buffDurationMs = 20000
    val buff = object : Buff() {
        override val name: String  = "Pendant of the Violet Eye"
        override val durationMs: Int = buffDurationMs

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CAST
            )
            override val type: Type = Type.STATIC
            override fun cooldownMs(sp: SimParticipant): Int = 0
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?){
                val state = stackBuff.state(sp)
                if (state.currentStacks < stackBuff.maxStacks) {
                    sp.addBuff(stackBuff)
                }
            }
        }

        override fun modifyStats(sp: SimParticipant): Stats? {
            val state = stackBuff.state(sp)
            return Stats(manaPer5Seconds = 21 * state.currentStacks)
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }
    val stackBuff = object : Buff() {
        override val name: String = "Violet Eye Mana Stack"
        override val durationMs: Int = -1
        override val maxStacks: Int = 100
        override val hidden: Boolean = true
    }

    val ability = object : Ability() {
        override val id: Int = 29601
        override val name: String = "Pendant of the Violet Eye"
        override fun gcdMs(sp: SimParticipant): Int = 0
        override fun cooldownMs(sp: SimParticipant): Int = 120000

        override fun trinketLockoutMs(sp: SimParticipant): Int = buffDurationMs

        override fun cast(sp: SimParticipant) {
            val state = stackBuff.state(sp)
            sp.addBuff(buff)
            if(state.currentStacks > 1){
                sp.consumeBuff(stackBuff)
                //remove stacks from previous application when the trinket is used
            }
        }
    }

    override fun activeTrinketAbility(sp: SimParticipant): Ability = ability
}
