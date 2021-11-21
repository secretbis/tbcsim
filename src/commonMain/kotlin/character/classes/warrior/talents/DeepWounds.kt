package character.classes.warrior.talents

import character.*
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class DeepWounds(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Deep Wounds"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val bloodFrenzy = object : Buff() {
        override val name: String = "Blood Frenzy (DW)"
        override val icon: String = "ability_warrior_bloodfrenzy.jpg"
        override val durationMs: Int = 12000

        override fun modifyStats(sp: SimParticipant): Stats {
            val bfTalent = sp.character.klass.talents[BloodFrenzy.name] as BloodFrenzy?
            return Stats(physicalDamageMultiplier = 1.0 + ((bfTalent?.currentRank ?: 0) * 0.02))
        }
    }

    fun bleedDebuff(owner: SimParticipant) = object : Debuff(owner) {
        override val name: String = "Deep Wounds"
        override val icon: String = "ability_backstab.jpg"
        override val durationMs: Int = 12000
        override val tickDeltaMs: Int = 3000

        val deepWoundsAbility = object : Ability() {
            override val name: String = Companion.name
            override val icon: String = "ability_warrior_bloodfrenzy.jpg"
        }

        override fun tick(sp: SimParticipant) {
            // 20% of average weapon damage per talent point over  full duration
            val tickPct = tickDeltaMs.toDouble() / durationMs.toDouble()
            // TODO: Using the mainhand every time may or may not be an approximation when dual wielding
            val damageFullDuration = owner.character.gear.mainHand.avgDmg * 0.2 * currentRank

            owner.logEvent(Event(
                eventType = EventType.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                ability = deepWoundsAbility,
                amount = damageFullDuration * tickPct,
                result = EventResult.HIT,
            ))
        }
    }

    val staticBuff = object : Buff() {
        override val name: String = "Deep Wounds (static)"
        override val icon: String = "ability_backstab.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_CRIT
            )

            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.sim.target.addDebuff(bleedDebuff(sp))

                val hasBfRanks = sp.character.klass.talents[BloodFrenzy.name]?.currentRank ?: 0 > 0
                if(hasBfRanks) {
                    sp.addBuff(bloodFrenzy)
                }
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(staticBuff)
}
