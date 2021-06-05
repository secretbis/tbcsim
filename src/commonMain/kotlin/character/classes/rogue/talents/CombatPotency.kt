package character.classes.rogue.talents

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant
import mechanics.Melee
import kotlin.random.Random

class CombatPotency(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Combat Potency"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val percentChance = 20.0

    fun energyGenerated(): Int {
        return currentRank * 3
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_YELLOW_HIT,  // shiv for example
                Trigger.MELEE_YELLOW_CRIT,
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = percentChance

            override fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
                // Only offhand attacks
                val item = items?.get(0) ?: return false
                val isOffhand = Melee.isOffhand(sp, item)
                return isOffhand && super.shouldProc(sp, items, ability, event)
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(energyGenerated(), Resource.Type.ENERGY, name)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}