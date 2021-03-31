package character.classes.hunter.pet.buffs

import character.*
import character.classes.hunter.talents.BestialDiscipline
import data.model.Item
import sim.Event
import sim.SimParticipant

class PetFocusRegen : Buff() {
    override val name: String = "Focus Regen"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val baseFocusRegen: Double = 5.0

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SERVER_TICK
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            val bestialDiscTalent = sp.owner?.character?.klass?.talents?.get(BestialDiscipline.name) as BestialDiscipline?
            val bdMultiplier = bestialDiscTalent?.petFocusRegenMultiplier() ?: 1.0

            // Focus regen is per second, but server tick isn't every second
            val tickModifier: Double = sp.sim.serverTickMs / 1000.0
            sp.addResource((baseFocusRegen * tickModifier * bdMultiplier).toInt(), Resource.Type.FOCUS, name)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
