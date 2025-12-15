package character.classes.common.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Resource
import data.model.Item
import mechanics.General
import sim.Event
import sim.SimParticipant

class SpiritRegen : Buff() {
    companion object {
        const val name = "Mana Regen (Spirit)"
    }

    override val name: String = Companion.name
    override val icon: String = "spell_magic_managain.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val regenAbility = object : Ability() {
        override val name: String = Companion.name
        override val icon: String = "spell_magic_managain.jpg"
    }

    val regenProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SERVER_TICK
        )
        override val type: Type = Type.STATIC

        override fun proc(
            sp: SimParticipant,
            items: List<Item>?,
            ability: Ability?,
            event: Event?
        ) {
            // Assume we are always casting in a sim
            val oocRegen = General.regenFromSpiritNotCasting(sp)
            val pctRegenInCombat = sp.stats.spiritRegenInCombatPct
            val regenMultiplier = sp.stats.spiritRegenInCombatMultiplier
            val restored = oocRegen * pctRegenInCombat * regenMultiplier

            if(restored > 0.0) {
                sp.addResource(restored.toInt(), Resource.Type.MANA, regenAbility)
            }
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> {
        return listOf(regenProc)
    }
}
