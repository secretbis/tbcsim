package data.itemsets

import character.*
import character.classes.druid.Druid
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class MalorneHarness : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Malorne Harness (2 set)"
        const val FOUR_SET_BUFF_NAME = "Malorne Harness (4 set)"
    }
    override val id: Int = 640

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1

        val bearCatProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_GLANCE,
                Trigger.MELEE_BLOCK
            )

            // Per the internet, the proc chance is the same per form
            // If it turns out different, this will need to be split
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 4.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                // TODO: Figure out druid forms for reals
                if(sp.buffs[Druid.BEAR_FORM_BUFF_NAME] != null || sp.buffs[Druid.DIRE_BEAR_FORM_BUFF_NAME] != null) {
                    sp.addResource(10, Resource.Type.RAGE, FOUR_SET_BUFF_NAME)
                }

                if(sp.buffs[Druid.CAT_FORM_BUFF_NAME] != null) {
                    sp.addResource(20, Resource.Type.ENERGY, FOUR_SET_BUFF_NAME)
                }
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(bearCatProc)
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats? {
            // TODO: Figure out druid forms for reals
            if(sp.buffs[Druid.BEAR_FORM_BUFF_NAME] != null || sp.buffs[Druid.DIRE_BEAR_FORM_BUFF_NAME] != null) {
                return Stats(armor = 1400)
            }

            if(sp.buffs[Druid.CAT_FORM_BUFF_NAME] != null) {
                return Stats(strength = 30)
            }

            return null
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
