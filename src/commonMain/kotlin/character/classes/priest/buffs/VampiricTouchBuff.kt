package character.classes.priest.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Resource
import data.Constants
import data.model.Item
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class VampiricTouchBuff : Buff() {
    companion object {
        const val name: String = "Vampiric Touch (Restore Mana)"
    }

    override val id = 34919
    override val name: String = Companion.name
    override val icon: String = "spell_holy_stoicism.jpg"
    override val durationMs: Int = 15000

    val manaRestoreMultiplier = 0.05

    val vtAbility = object : Ability() {
        override val name: String = Companion.name
        override val icon: String = "spell_holy_stoicism.jpg"
    }

    val shadowDamageProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SHADOW_DAMAGE_NON_PERIODIC,
            Trigger.SHADOW_DAMAGE_PERIODIC,
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            if (event == null || ability == null) return

            val manaRestore = (event.amount * manaRestoreMultiplier).toInt();

            sp.addResource(manaRestore, Resource.Type.MANA, vtAbility)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(shadowDamageProc)
}
