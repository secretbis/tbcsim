package character.classes.priest.talents

import character.*
import character.classes.priest.debuffs.ShadowWeavingDebuff
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

import kotlin.random.Random

class ShadowWeaving(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shadow Weaving"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SHADOW_DAMAGE_NON_PERIODIC
            )
            override val type: Type = Type.STATIC            

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                if(event?.result != EventResult.RESIST) {
                    val debuff = sp.sim.target.debuffs.get(Companion.name) ?: ShadowWeavingDebuff(sp)

                    val applyChance = Random.nextDouble()
                    if(applyChance <= currentRank * 0.2){
                        sp.sim.target.addDebuff(debuff)
                    }
                }
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
