package data.model.default

import character.Ability
import character.Proc
import data.model.Item
import mu.KotlinLogging
import sim.SimIteration

class UnknownProc : Proc() {
    override val type: Type = Type.STATIC

    override val triggers: List<Trigger>
        get() = Trigger.values().asList()

    override fun shouldProc(sim: SimIteration, items: List<Item>?, ability: Ability?): Boolean {
        return true
    }

    override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
        val itemStr = items?.map { "item: ${it.name} (${it.id})" }?.joinToString { ", " } ?: ""
        logger.warn { "Unknown proc ${ability?.name} (${ability?.id}) $itemStr" }
    }
}
