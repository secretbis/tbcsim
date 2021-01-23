package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

class Stormstrike(sim: SimIteration) : Ability(sim) {
    override val id: Int = 17364
    override val name: String = "Stormstrike"
    override val cooldownMs: Double = 10.0

    val buff = object : Buff() {
        override val name: String = "Stormstrike"
        override val durationMs: Int = 12000
        override val maxCharges: Int = 2

        // Increase nature damage for as long as we have charges
        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            return stats.add(
                Stats(
                    natureDamageMultiplier = 1.2
                )
            )
        }

        // Proc off of nature damage to reduce our stacks
        override fun procs(sim: SimIteration): List<Proc> {
            return listOf(
                object : Proc() {
                    override val triggers: List<Trigger> = listOf(
                        Trigger.NATURE_DAMAGE
                    )
                    override val type: Type = Type.STATIC

                    override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                        val state = state(sim)
                        state.currentCharges -= 1
                    }
                }
            )
        }
    }

    override fun cast(free: Boolean) {
        super.cast(free)

        // Do attacks
        val mhItem = sim.subject.gear.mainHand
        val mhAttack = Melee.baseDamageRoll(sim, mhItem, isNormalized = true)
        val mhResult = Melee.attackRoll(sim, mhAttack, isWhiteDmg = false)

        val ohItem = sim.subject.gear.offHand
        val ohAttack = Melee.baseDamageRoll(sim, ohItem, isNormalized = true)
        val ohResult = Melee.attackRoll(sim, ohAttack, isWhiteDmg = false, isOffHand = true)

        // TODO: Distinguish MH and OH events in the logs?
        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = mhResult.first,
            result = mhResult.second,
        ))

        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = ohResult.first,
            result = ohResult.second,
        ))

        // Proc anything that can proc off a yellow hit
        // TODO: Should I fire procs off miss/dodge/parry/etc?
        val triggerTypes = when(mhResult.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(mhItem), this)
        }

        val triggerTypesOh = when(ohResult.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypesOh != null) {
            sim.fireProc(triggerTypesOh, listOf(ohItem), this)
        }
    }

    override val baseCastTimeMs: Int = 0
    override val gcdMs: Int = sim.subject.physicalGcd().toInt()
}
