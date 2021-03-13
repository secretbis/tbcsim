package character.classes.pet.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

class Frenzy(owner: Character) : Buff() {
    override val name: String = "Frenzy (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

//    val buff = object : Buff() {
//        override val name: String = "Frenzy (Pet)"
//        override val durationMs: Int = 8000
//
//        override fun modifyStats(sp: SimParticipant): Stats? {
//            return
//        }
//    }
//
//    val proc = object : Proc() {
//        override val triggers: List<Trigger> = listOf(
//            Trigger.PET_MELEE_CRIT
//        )
//        override val type: Type = Type.PERCENT
//        override fun percentChance(sp: SimParticipant): Double {
//            val frenzyTalentRanks = owner.klass.talents[FrenzyTalent.name]?.currentRank ?: 0
//            return 20.0 * frenzyTalentRanks
//        }
//
//        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
//            sim.addPetBuff(buff)
//        }
//    }
//
//    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
