package data.model.default

import character.Ability
import character.Proc
import data.model.Item
import mu.KotlinLogging
import sim.SimIteration

class UnknownProc : Proc() {
    val logger = KotlinLogging.logger {}

    override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
        val itemStr = items?.map { "item: ${it.name} (${it.id})" }?.joinToString { ", " } ?: ""
        logger.warn { "Unknown proc ${ability?.name} (${ability?.id}) $itemStr" }
    }
}
