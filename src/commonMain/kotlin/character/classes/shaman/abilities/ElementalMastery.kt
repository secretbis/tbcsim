package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class ElementalMastery : Ability() {
    companion object {
        const val name = "Elemental Mastery"
    }
    override val id: Int = 0
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    private fun makeProc(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.FIRE_DAMAGE_NON_PERIODIC,
                Trigger.FROST_DAMAGE,
                Trigger.NATURE_DAMAGE,
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                // Remove the crit buff
                sp.consumeBuff(buff)

                // Refund the resource cost of the triggering ability
                if(ability != null) {
                    sp.addResource(ability.resourceCost(sp).toInt(), ability.resourceType(sp), name)
                }
            }
        }
    }

    val emBuff = object : Buff() {
        override val name: String = "Elemental Mastery (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val maxCharges: Int = 1

        val proc = makeProc(this)

        override fun modifyStats(sp: SimParticipant): Stats {
            // As with Elemental Precision, this is accurate for practical Shaman purposes
            return Stats(spellCritRating = 100.0 * Rating.critPerPct)
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(emBuff)
    }
}
