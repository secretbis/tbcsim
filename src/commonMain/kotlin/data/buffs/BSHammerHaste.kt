package data.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimIteration
import sim.SimParticipant

// https://tbc-twinhead.twinstar.cz/?spell=21165
// Same for all the hammers
class BSHammerHaste(val sourceItem: Item) : ItemBuff(listOf(sourceItem)) {
    companion object {
        private var singletonBuff: Buff? = null

        fun singletonBuff(): Buff {
            if(singletonBuff == null) {
                singletonBuff = object : Buff() {
                    override val name: String = "Haste (BS Hammer)"

                    override val durationMs: Int = 10000

                    override fun modifyStats(sp: SimParticipant): Stats? {
                        return Stats(physicalHasteRating = 212.0)
                    }
                }
            }

            return singletonBuff!!
        }
    }

    override val id: Int = 21165
    override val name: String = "Haste (BS Hammer) (static) ${sourceItem.uniqueName}"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    private var _procs: List<Proc>? = null
    private fun makeProcs(sp: SimParticipant): List<Proc> {
        if(_procs == null) {
            _procs = listOf(
                object : Proc() {
                    override val triggers: List<Trigger> = listOf(
                        Trigger.MELEE_AUTO_HIT,
                        Trigger.MELEE_AUTO_CRIT,
                        Trigger.MELEE_WHITE_HIT,
                        Trigger.MELEE_WHITE_CRIT,
                        Trigger.MELEE_YELLOW_HIT,
                        Trigger.MELEE_YELLOW_CRIT,
                        Trigger.MELEE_BLOCK,
                        Trigger.MELEE_GLANCE
                    )

                    override val type: Type = Type.PPM
                    override val ppm: Double = 1.0
                    override val requiresItem: Boolean = true

                    override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                        sp.addBuff(singletonBuff())
                    }
                }
            )
        }

        return _procs!!
    }

    override fun procs(sp: SimParticipant): List<Proc> = makeProcs(sp)
}
