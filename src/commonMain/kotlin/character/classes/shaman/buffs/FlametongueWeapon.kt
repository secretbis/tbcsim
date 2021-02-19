package character.classes.shaman.buffs

import character.classes.shaman.abilities.FlametongueWeapon as FlametongueWeaponAbility
import character.Ability
import character.ItemBuff
import character.ItemProc
import character.Proc
import data.model.Item
import sim.Event
import sim.SimIteration

class FlametongueWeapon(sourceItem: Item) : ItemBuff(listOf(sourceItem)) {
    companion object {
        const val name = "Flametongue Weapon"
    }
    override val id: Int = 25489
    override val name = "Flametongue Weapon (static) ${sourceItem.uniqueName}"
    override val durationMs: Int = 30 * 60 * 1000

    val proc = object : ItemProc(sourceItems) {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT
        )

        override val type: Type = Type.STATIC

        var ftAbility: Ability? = null

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
            if (ftAbility == null) {
                val suffix = when (sourceItem) {
                    sim.subject.gear.mainHand -> "(MH)"
                    sim.subject.gear.offHand -> "(OH)"
                    else -> "(Unknown)"
                }
                val name = "Flametongue Weapon $suffix"
                ftAbility = FlametongueWeaponAbility(name, sourceItem)
            }

            if (ftAbility!!.available(sim)) {
                ftAbility!!.cast(sim)

                sim.logEvent(
                    Event(
                        eventType = Event.Type.PROC,
                        abilityName = ftAbility!!.name
                    )
                )
            }
        }
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
}
