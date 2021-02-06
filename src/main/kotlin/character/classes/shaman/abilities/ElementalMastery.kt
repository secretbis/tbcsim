package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimIteration

class ElementalMastery : Ability() {
    companion object {
        const val name = "Elemental Mastery"
    }
    override val id: Int = 0
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = 0

    private fun makeProc(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.FIRE_DAMAGE,
                Trigger.FROST_DAMAGE,
                Trigger.NATURE_DAMAGE,
            )
            override val type: Type = Type.STATIC

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                // Remove the crit buff
                sim.consumeBuff(buff)

                // Refund the resource cost of the triggering ability
                if(ability != null) {
                    sim.addResource(ability.resourceCost(sim).toInt(), ability.resourceType(sim))
                }
            }
        }
    }

    val emBuff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val maxCharges: Int = 1

        val proc = makeProc(this)

        override fun modifyStats(sim: SimIteration): Stats {
            // As with Elemental Precision, this is accurate for practical Shaman purposes
            return Stats(spellCritRating = 100.0 * Rating.critPerPct)
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(emBuff)
    }
}
