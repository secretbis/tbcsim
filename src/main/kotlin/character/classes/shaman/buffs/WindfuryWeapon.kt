package character.classes.shaman.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimIteration
import kotlin.random.Random
import character.classes.shaman.abilities.WindfuryWeapon as WindfuryWeaponAbility

class WindfuryWeapon(val sourceItem: Item) : Buff() {
    class WindfuryWeaponState : Buff.State() {
        var lastWindfuryWeaponProcMs: Int = -1
    }

    override fun stateFactory(): WindfuryWeaponState {
        return WindfuryWeaponState()
    }

    override val name = "Windfury Weapon"
    override val durationMs: Int = 30 * 60 * 1000

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats
    }

    // Windfury weapon has a global 3s ICD, regardless of rank
    val icdMs = 3000

    override val procs: List<Proc>
        get() = listOf(
            object : Proc() {
                override val triggers: List<Trigger> = listOf(
                    Trigger.MELEE_WHITE_HIT,
                    Trigger.MELEE_WHITE_CRIT,
                    Trigger.MELEE_YELLOW_HIT,
                    Trigger.MELEE_YELLOW_CRIT
                )

                override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                    val state = state(sim) as WindfuryWeaponState

                    val lastProc = state.lastWindfuryWeaponProcMs
                    val offIcd = lastProc == -1 || lastProc + icdMs <= sim.elapsedTimeMs

                    if(offIcd && items?.contains(sourceItem) == true) {
                        val wfAbility = WindfuryWeaponAbility(sim, sourceItem)

                        // 20% chance to trigger
                        val rng = Random.nextDouble() < 0.20

                        if(rng && wfAbility.available()) {
                            wfAbility.cast()
                            sim.logEvent(
                                Event(
                                    eventType = Event.Type.PROC,
                                    ability = wfAbility
                                )
                            )
                        }
                    }
                }
            }
        )
}
