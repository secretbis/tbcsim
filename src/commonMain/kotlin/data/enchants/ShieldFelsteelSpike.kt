package data.enchants

import character.Ability
import character.Proc
import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant
import kotlin.js.JsExport
import kotlin.random.Random

@JsExport
class ShieldFelsteelSpike(item: Item) : Enchant(item) {
    override val id: Int = 29454
    override val inventorySlot: Int = Constants.InventorySlot.SHIELD.ordinal
    override val name: String = "Felsteel Shield Spike"
    override val icon: String = "inv_misc_armorkit_27.jpg"

    val spikeAbility = object : Ability() {
        override val name: String = "Felsteel Shield Spike"
        override val icon: String = "inv_misc_armorkit_27.jpg"
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.INCOMING_MELEE_BLOCK
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            val damage = Random.nextDouble(26.0, 38.0)
            sp.logEvent(
                Event(
                    eventType = EventType.DAMAGE,
                    damageType = Constants.DamageType.PHYSICAL,
                    amount = damage,
                    ability = spikeAbility,
                    result = EventResult.HIT
                )
            )
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
