package data.buffs

import character.*
import data.model.Item
import sim.SimIteration

// https://tbc-twinhead.twinstar.cz/?spell=21165
// Same for all the hammers
class BSHammerHaste(val sourceItem: Item) : ItemBuff(listOf(sourceItem)) {
    override val id: Int = 21165
    override val name: String = "Haste (BS Hammer)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    private var _procs: List<Proc>? = null
    private fun makeProcs(sim: SimIteration): List<Proc> {
        if(_procs == null) {
            _procs = listOf(
                object : Proc() {
                    override val triggers: List<Trigger> = listOf(
                        Trigger.MELEE_AUTO_HIT,
                        Trigger.MELEE_AUTO_CRIT,
                        Trigger.MELEE_WHITE_HIT,
                        Trigger.MELEE_WHITE_CRIT,
                        Trigger.MELEE_YELLOW_HIT,
                        Trigger.MELEE_YELLOW_CRIT
                    )

                    override val type: Type = Type.PPM
                    override val ppm: Double = 1.0
                    override val requiresItem: Boolean = true

                    val buff = object : Buff() {
                        override val name: String
                            get() {
                                val suffix = if (sourceItem === sim.subject.gear.mainHand) {
                                    "(MH)"
                                } else if (sourceItem === sim.subject.gear.offHand) {
                                    "(OH)"
                                } else {
                                    throw IllegalArgumentException("BSHammerHaste can only be applied to weapons")
                                }
                                return "Haste (BS Hammer) $suffix".trim()
                            }

                        override val durationMs: Int = 10000

                        override fun modifyStats(sim: SimIteration): Stats? {
                            return Stats(physicalHasteRating = 212.0)
                        }
                    }

                    override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                        sim.addBuff(buff)
                    }
                }
            )
        }

        return _procs!!
    }

    override fun procs(sim: SimIteration): List<Proc> = makeProcs(sim)
}
