package data.default

import character.Ability
import character.Proc
import data.model.Item
import sim.Event
import sim.SimIteration

class UnknownProc : Proc() {
    override val type: Type = Type.STATIC

    override val triggers: List<Trigger>
        get() = Trigger.values().asList()

    override fun shouldProc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
        return true
    }

    override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
        val itemStr = items?.map { "item: ${it.name} (${it.id})" }?.joinToString { ", " } ?: ""
        logger.warn { "Unknown proc ${ability?.name} (${ability?.id}) $itemStr" }
    }
}
