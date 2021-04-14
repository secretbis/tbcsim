package character.classes.shaman.buffs

import character.*
import character.classes.shaman.abilities.WindfuryWeapon
import data.Constants
import data.model.Item
import data.model.TempEnchant
import sim.Event
import sim.SimParticipant

class WindfuryWeapon(sourceItem: Item) : TempEnchant(sourceItem) {
    class WindfuryWeaponState : Buff.State() {
        var lastWindfuryWeaponProcMs: Int = -1
    }

    override fun stateFactory(): WindfuryWeaponState {
        return WindfuryWeaponState()
    }

    override val name = "Windfury Weapon (static) ${sourceItem.uniqueName}"
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val durationMs: Int = 30 * 60 * 1000
    override val hidden: Boolean = true

    override fun modifyStats(sp: SimParticipant): Stats? {
        // Mark each mainhand weapon as having this instead
        // FIXME: This needs to be removed if this buff expires or is otherwise removed
        sourceItems.first().tempEnchant = this
        return null
    }

    private fun buffState(sp: SimParticipant): WindfuryWeaponState {
        return this.state(sp, "Windfury Weapon") as WindfuryWeaponState
    }

    // Windfury weapon has a global 3s ICD, regardless of rank
    val icdMs = 3000
    val proc = object : ItemProc(sourceItems) {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT
        )

        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double {
            val mhWf = sp.character.gear.mainHand.tempEnchant?.name?.startsWith("Windfury Weapon") == true
            val ohWf = sp.character.gear.offHand.tempEnchant?.name?.startsWith("Windfury Weapon") == true

            return if(mhWf && ohWf) 36.0 else 20.0
        }

        override fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
            // Check shared WF ICD state
            val state = buffState(sp)

            val lastProc = state.lastWindfuryWeaponProcMs
            val offIcd = lastProc == -1 || lastProc + icdMs <= sp.sim.elapsedTimeMs

            return offIcd && super.shouldProc(sp, items, ability, event)
        }

        var wfAbility: Ability? = null

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            if(wfAbility == null) {
                val suffix = when(sourceItem) {
                    sp.character.gear.mainHand -> "(MH)"
                    sp.character.gear.offHand -> "(OH)"
                    else -> "(Unknown)"
                }
                val name = "Windfury Weapon $suffix"
                wfAbility = WindfuryWeapon(name, sourceItem)
            }

            if(wfAbility!!.available(sp)) {
                wfAbility!!.cast(sp)

                // Update ICD state
                val state = buffState(sp)
                state.lastWindfuryWeaponProcMs = sp.sim.elapsedTimeMs

                sp.logEvent(
                    Event(
                        eventType = Event.Type.PROC,
                        abilityName = wfAbility!!.name
                    )
                )
            }
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
