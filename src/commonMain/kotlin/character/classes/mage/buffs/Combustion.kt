package character.classes.mage.buffs

import character.Ability
import character.Buff
import character.Proc
import character.classes.mage.abilities.FireBlast
import character.classes.mage.abilities.Fireball
import character.classes.mage.abilities.Scorch
import data.model.Item
import sim.Event
import sim.SimParticipant

class Combustion : Buff() {
    companion object {
        const val name = "Combustion"
    }
    override val name: String = Companion.name
    override val icon: String = "spell_fire_sealoffire.jpg"
    override val durationMs: Int = -1
    override val maxStacks: Int = 10
    override val maxCharges: Int = 3

    val affectedSpells = listOf(
        Fireball.name,
        FireBlast.name,
        Scorch.name
    )

    fun consumeProc(buff: Buff): Proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SPELL_CRIT
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            if(affectedSpells.contains(ability?.name)) {
                sp.consumeBuff(buff)
            }
        }
    }

    fun stackProc(buff: Buff): Proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SPELL_HIT
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            if(affectedSpells.contains(ability?.name)) {
                sp.addBuff(buff)
            }
        }
    }

    fun getFireSpellAddlCritPct(sp: SimParticipant): Double {
        val state = state(sp)
        return 0.1 * state.currentStacks
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(
        consumeProc(this),
        stackProc(this)
    )
}
