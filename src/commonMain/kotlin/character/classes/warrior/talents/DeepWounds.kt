package character.classes.warrior.talents

import character.*
import data.Constants
import data.model.Item
import sim.Event
import sim.SimIteration

class DeepWounds(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Deep Wounds"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val bloodFrenzy = object : Buff() {
        override val name: String = "Blood Frenzy (DW)"
        override val durationMs: Int = 12000

        override fun modifyStats(sim: SimIteration): Stats {
            val bfTalent = sim.subject.klass.talents[BloodFrenzy.name] as BloodFrenzy?
            return Stats(physicalDamageMultiplier = 1.0 + ((bfTalent?.currentRank ?: 0) * 0.02))
        }
    }

    val bleedDebuff = object : Debuff() {
        override val name: String = Companion.name
        override val durationMs: Int = 12000
        override val tickDeltaMs: Int = 3000

        override fun tick(sim: SimIteration) {
            // 20% of average weapon damage per talent point over  full duration
            val tickPct = tickDeltaMs.toDouble() / durationMs.toDouble()
            // TODO: Using the mainhand every time may or may not be an approximation when dual wielding
            val damageFullDuration = sim.subject.gear.mainHand.avgDmg * 0.2 * currentRank

            sim.logEvent(Event(
                eventType = Event.Type.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                abilityName = name,
                amount = damageFullDuration * tickPct,
                result = Event.Result.HIT,
            ))
        }
    }

    val staticBuff = object : Buff() {
        override val name: String = "Deep Wounds (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_CRIT
            )

            override val type: Type = Type.STATIC

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                sim.addDebuff(bleedDebuff)

                val hasBfRanks = sim.subject.klass.talents[BloodFrenzy.name]?.currentRank ?: 0 > 0
                if(hasBfRanks) {
                    sim.addBuff(bloodFrenzy)
                }
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(staticBuff)
}
