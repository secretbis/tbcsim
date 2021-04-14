package data

import data.itemscustom.EmptyItem
import data.model.Item
import data.tempenchants.*
import kotlin.js.JsExport

@JsExport
object TempEnchants {
    val tempEnchants = arrayOf(
        { item: Item -> AdamantiteStone(item) },
        { item: Item -> BrilliantWizardOil(item) },
        { item: Item -> BuggedHunterAdamantiteStone(item) },
        { item: Item -> ElementalStone(item) },
        { item: Item -> SuperiorWizardOil(item) },
    )

    val byName = tempEnchants.map {
        val eval = it(EmptyItem())
        eval.name to { item: Item -> it(item) }
    }.toMap()
    val byId = tempEnchants.map { val eval = it(EmptyItem()); eval.id to { item: Item -> it(item) } }.toMap()
    val bySlot = tempEnchants.groupBy { val eval = it(EmptyItem()); eval.inventorySlot }.mapValues { it.value.toTypedArray() }
}
