package character.classes.hunter.pet.buffs

import character.*
import character.classes.hunter.talents.Frenzy
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class PetFrenzy : Buff() {
    override val name: String = "Frenzy (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val icon: String = "inv_misc_monsterclaw_03.jpg"

    val frenzyProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_CRIT
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double {
            val frenzyTalent = sp.owner?.character?.klass?.talents?.get(Frenzy.name) as Frenzy?
            return frenzyTalent?.petFrenzyProcChancePct() ?: 0.0
        }

        val frenzyBuff = object : Buff() {
            override val name: String = "Frenzy"
            override val icon: String = "inv_misc_monsterclaw_03.jpg"
            override val durationMs: Int = 8000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(physicalHasteRating = 30.0 * Rating.hastePerPct)
            }
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(frenzyBuff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(frenzyProc)
}
