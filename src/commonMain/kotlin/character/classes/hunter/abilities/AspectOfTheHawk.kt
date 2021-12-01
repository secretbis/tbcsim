package character.classes.hunter.abilities

import character.*
import character.classes.hunter.talents.ImprovedAspectOfTheHawk
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class AspectOfTheHawk : Ability() {
    companion object {
        const val name = "Aspect of the Hawk"
    }

    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun resourceCost(sp: SimParticipant): Double = 120.0
    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.MANA

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val icon: String = "spell_nature_ravenform.jpg"
        override val mutex: List<Mutex> = listOf(Mutex.BUFF_HUNTER_ASPECT)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(rangedAttackPower = 120)
        }

        val hasteBuff = object : Buff() {
            override val name: String = ImprovedAspectOfTheHawk.name
            override val durationMs: Int = 12000
            override val icon: String = "spell_nature_ravenform.jpg"

            override fun modifyStats(sp: SimParticipant): Stats {
                val currentRank = sp.character.klass.talentRanks(ImprovedAspectOfTheHawk.name)
                return Stats(physicalHasteRating = 3.0 * currentRank * Rating.hastePerPct)
            }
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.RANGED_AUTO_HIT,
                Trigger.RANGED_AUTO_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 10.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(hasteBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
