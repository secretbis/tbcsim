package data.itemsets

import character.*
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class MalorneRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Malorne Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Malorne Regalia (4 set)"

        fun fourSetInnervateCooldownReductionMs(): Int {
            return 48000
        }
    }
    override val id: Int = 639

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1

        val spellProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_HIT,
                Trigger.SPELL_CRIT
            )

            // TODO: Proc chance unknown, setting to 1PPM for now
            override val type: Type = Type.PPM
            override val ppm: Double = 1.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(120, Resource.Type.MANA, TWO_SET_BUFF_NAME)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(spellProc)
    }

    // TODO: Innervate should check this buff once it exists
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
