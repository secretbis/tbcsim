package character.classes.priest.pet.buffs

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

// https://tbc.wowhead.com/spell=28305/mana-leech
// https://tbc.wowhead.com/spell=34650/mana-leech
class ManaLeech : Buff() {
    companion object {
        const val name = "Mana Leech"
    }

    override val id = 28305
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_shadowmend.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val mlAbility = object : Ability() {
        override val name: String = Companion.name
        override val icon: String = "spell_shadow_shadowmend.jpg"
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_AUTO_HIT,
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            if (sp.owner == null || event == null) return

            // https://web.archive.org/web/20071201221602/http://www.shadowpriest.com/viewtopic.php?t=7616
            sp.owner.addResource((event.amount * 2.5).toInt(), Resource.Type.MANA, mlAbility)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
