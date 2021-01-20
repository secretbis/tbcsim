package character.classes.shaman.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimIteration
import kotlin.random.Random
import character.classes.shaman.abilities.WindfuryWeapon as WindfuryWeaponAbility

class WindfuryWeapon(val sourceItem: Item) : Buff() {
    override var appliedAtMs: Int = 0
    override val durationMs: Int = 30 * 60 * 1000
    override val statModType: ModType = ModType.NONE

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats
    }

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
                    if(items?.contains(sourceItem) == true) {
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
