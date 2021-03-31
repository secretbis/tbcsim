package character.classes.hunter.pet.buffs

import character.*
import character.classes.hunter.talents.FerociousInspiration
import data.model.Item
import sim.Event
import sim.SimParticipant

class PetFerociousInspiration : Buff() {
        override val name: String = "Ferocious Inspiration (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        fun getFIRanks(sp: SimParticipant): Int {
            return sp.owner?.character?.klass?.talents?.get(FerociousInspiration.name)?.currentRank ?: 0
        }

        val apBuff = object : Buff() {
            override val name: String = "Ferocious Inspiration"
            override val durationMs: Int = 10000

            override fun modifyStats(sp: SimParticipant): Stats {
                val mult = 1.0 + (0.01 * getFIRanks(sp))
                return Stats(
                    physicalDamageMultiplier = mult,
                    spellDamageMultiplier = mult
                )
            }
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 100.0 * (getFIRanks(sp) / 3.0)

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.sim.addRaidBuff(apBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }
