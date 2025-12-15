package data.abilities.raid

import character.Ability
import character.Buff
import character.Proc
import character.Resource
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class VampiricTouch(val dps: Int): Ability() {
    override val id: Int = 34917
    override val name: String = "Vampiric Touch ($dps DPS)"
    override val icon: String = "spell_holy_stoicism.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val manaProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.SERVER_FIVE_SECOND_TICK)
        override val type: Type = Type.STATIC

        override fun proc(
            sp: SimParticipant,
            items: List<Item>?,
            ability: Ability?,
            event: Event?
        ) {
            val manaRestored = (0.05 * dps * 5.0).toInt()
            sp.addResource(manaRestored, Resource.Type.MANA, this@VampiricTouch)
        }
    }

    val buff = object : Buff() {
        override val name: String = "Vampiric Touch ($dps DPS)"
        override val icon: String = "spell_holy_stoicism.jpg"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun procs(sp: SimParticipant): List<Proc> {
            return listOf(manaProc)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(buff)
    }
}
