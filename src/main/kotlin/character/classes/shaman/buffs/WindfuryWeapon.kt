package character.classes.shaman.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimIteration
import kotlin.random.Random
import character.classes.shaman.abilities.WindfuryWeapon as WindfuryWeaponAbility

class WindfuryWeapon(sourceItem: Item) : ItemBuff(listOf(sourceItem)) {
    class WindfuryWeaponState : Buff.State() {
        var lastWindfuryWeaponProcMs: Int = -1
    }

    override fun stateFactory(): WindfuryWeaponState {
        return WindfuryWeaponState()
    }

    override val name = "Windfury Weapon"
    override val durationMs: Int = 30 * 60 * 1000
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats
    }

    // Windfury weapon has a global 3s ICD, regardless of rank
    val icdMs = 3000
    val proc = object : ItemProc(sourceItems) {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT
        )

        override val type: Type = Type.PERCENT
        override val percentChance: Double = 20.0

        override fun shouldProc(sim: SimIteration, items: List<Item>?, ability: Ability?): Boolean {
            // Check shared WF ICD state
            val state = sharedState(name, sim) as WindfuryWeaponState

            val lastProc = state.lastWindfuryWeaponProcMs
            val offIcd = lastProc == -1 || lastProc + icdMs <= sim.elapsedTimeMs

            return offIcd && super.shouldProc(sim, items, ability)
        }

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
            val wfAbility = WindfuryWeaponAbility(sim, sourceItems[0])
            if(wfAbility.available(sim)) {
                wfAbility.cast()

                // Update ICD state
                val state = sharedState(name, sim) as WindfuryWeaponState
                state.lastWindfuryWeaponProcMs = sim.elapsedTimeMs

                sim.logEvent(
                    Event(
                        eventType = Event.Type.PROC,
                        ability = wfAbility
                    )
                )
            }
        }
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
}
