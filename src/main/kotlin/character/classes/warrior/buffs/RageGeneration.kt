package character.classes.warrior.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Resource
import data.model.Item
import sim.Event
import sim.SimIteration

class RageGeneration : Buff() {
    override val name: String = "Rage Generation"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val procHit = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_BLOCK,
            Trigger.MELEE_GLANCE,
        )
        override val type: Type = Type.STATIC

        val rageConversionFactor = 274.7
        fun damageToRage(damage: Double, item: Item, weaponFactor: Double): Int {
            return (((damage / rageConversionFactor * 7.5) + (item.speed / 1000.0 * weaponFactor)) / 2).toInt()
        }

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
            val item = items?.get(0) ?: return
            val isOffhand = item === sim.subject.gear.offHand

            if(event?.eventType == Event.Type.DAMAGE) {
                val damage = event.amount
                val rage = if (isOffhand) {
                    damageToRage(damage, item, 1.75)
                } else {
                    damageToRage(damage, item, 3.5)
                }

                sim.addResource(rage, Resource.Type.RAGE)
            }
        }
    }

    val procCrit = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_AUTO_CRIT
        )
        override val type: Type = Type.STATIC

        val rageConversionFactor = 274.7
        fun damageToRage(damage: Double, item: Item, weaponFactor: Double): Int {
            return (((damage / rageConversionFactor * 7.5) + (item.speed / 1000.0 * weaponFactor)) / 2).toInt()
        }

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
            val item = items?.get(0) ?: return
            val isOffhand = item === sim.subject.gear.offHand

            if(event?.eventType == Event.Type.DAMAGE) {
                val damage = event.amount
                val rage = if (isOffhand) {
                    damageToRage(damage, item, 3.5)
                } else {
                    damageToRage(damage, item, 7.0)
                }

                sim.addResource(rage, Resource.Type.RAGE)
            }
        }
    }

    // TODO: Refund rage for missed yellow attacks, once I figure out how TBC does that
    //       May need to be implemented per-ability, if it's the same as Classic
    override fun procs(sim: SimIteration): List<Proc> = listOf(procHit, procCrit)
}
