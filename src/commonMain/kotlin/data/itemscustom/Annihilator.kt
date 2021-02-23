package data.itemscustom

import character.*
import data.Constants
import data.model.Item
import data.model.ItemSet
import data.model.Socket
import data.model.SocketBonus
import mechanics.Spell
import sim.Event
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class Annihilator : Item() {
    companion object {
        private var singletonDebuff: Debuff? = null

        fun singletonDebuff(): Debuff {
            if(singletonDebuff == null) {
                singletonDebuff = object : Debuff() {
                    override val name: String = "Annihilator"
                    override val durationMs: Int = 45000
                    override val maxStacks: Int = 3

                    override fun modifyStats(sim: SimIteration): Stats {
                        val currentStacks = state(sim).currentStacks
                        return Stats(armor = currentStacks * -200)
                    }
                }
            }

            return singletonDebuff!!
        }
    }

    override var id: Int = 12798
    override var name: String = "Annihilator"
    override var itemLevel: Int = 61
    override var itemSet: ItemSet? = null
    override var itemClass: Constants.ItemClass? = Constants.ItemClass.WEAPON
    override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.AXE_1H
    override var minDmg: Double = 49.0
    override var maxDmg: Double = 92.0
    override var speed: Double = 1700.0
    override var stats: Stats = Stats()
    override var sockets: Array<Socket> = arrayOf()
    override var socketBonus: SocketBonus? = null

    val staticBuff = object : Buff() {
        override val name: String = "Annihilator (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val armorProc = object : Proc() {
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
            override val type: Type = Type.PPM
            override val ppm: Double = 1.0

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                // This proc is Shadow school, and is resistable
                val result = Spell.attackRoll(sim, 1.0, Constants.DamageType.SHADOW, isBinary = true)
                if(result.second != Event.Result.RESIST) {
                    sim.addDebuff(singletonDebuff())
                }
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(armorProc)
    }

    override var buffs: List<Buff> = listOf(staticBuff)
}
