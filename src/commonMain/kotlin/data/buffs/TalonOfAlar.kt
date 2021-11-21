package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class TalonOfAlar : Buff() {
    companion object {
        const val name = "Talon of Al'ar"
    }
    override val name: String = Companion.name
    override val icon: String = "spell_fire_soulburn.jpg"
    override val durationMs: Int = -1

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(Trigger.HUNTER_CAST_ARCANE_SHOT)
        override val type: Type = Type.STATIC

        val buff = object : Buff() {
            override val name: String = Companion.name
            override val icon: String = "spell_fire_soulburn.jpg"
            override val durationMs: Int = 6000

            override fun modifyStats(sp: SimParticipant): Stats {
                // Doesn't apply to auto shot, RIP
                return Stats(
                    yellowDamageFlatModifier = 40.0
                )
            }
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
