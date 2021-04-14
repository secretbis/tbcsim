package data

import data.enchants.*
import data.itemscustom.EmptyItem
import data.model.Item
import kotlin.js.JsExport

@JsExport
object Enchants {
    val enchants = arrayOf(
        { item: Item -> Agility(item) },
        { item: Item -> BootsCatsSwiftness(item) },
        { item: Item -> BootsDexterity(item) },
        { item: Item -> BootsBoarsSpeed(item) },
        { item: Item -> BootsSurefooted(item) },
        { item: Item -> BracerAssault(item) },
        { item: Item -> BracerBrawn(item) },
        { item: Item -> BracerMajorIntellect(item) },
        { item: Item -> BracerSpellpower(item) },
        { item: Item -> ChestExceptionalStats(item) },
        { item: Item -> CloakGreaterAgility(item) },
        { item: Item -> CloakSpellPenetration(item) },
        { item: Item -> CobrahideLegArmor(item) },
        { item: Item -> Executioner(item) },
        { item: Item -> GlovesBlasting(item) },
        { item: Item -> GlovesMajorSpellpower(item) },
        { item: Item -> GlovesMajorStrength(item) },
        { item: Item -> GlovesSpellStrike(item) },
        { item: Item -> GlovesSuperiorAgility(item) },
        { item: Item -> GlyphOfFerocity(item) },
        { item: Item -> GlyphOfPower(item) },
        { item: Item -> GlyphOfTheOutcast(item) },
        { item: Item -> GreaterAgility(item) },
        { item: Item -> GreaterInscriptionOfDiscipline(item) },
        { item: Item -> GreaterInscriptionOfTheBlade(item) },
        { item: Item -> GreaterInscriptionOfTheOrb(item) },
        { item: Item -> GreaterInscriptionOfVengeance(item) },
        { item: Item -> KhoriumScope(item) },
        { item: Item -> MightOfTheScourge(item) },
        { item: Item -> Mongoose(item) },
        { item: Item -> MysticSpellthread(item) },
        { item: Item -> NethercobraLegArmor(item) },
        { item: Item -> RingSpellpower(item) },
        { item: Item -> RingStats(item) },
        { item: Item -> RingStriking(item) },
        { item: Item -> RunicSpellthread(item) },
        { item: Item -> Soulfrost(item) },
        { item: Item -> StabilizedEterniumScope(item) },
        { item: Item -> Sunfire(item) },
        { item: Item -> TwoHandMajorAgility(item) },
        { item: Item -> TwoHandSavagery(item) },
        { item: Item -> WeaponMajorSpellpower(item) },
        { item: Item -> WeaponPotency(item) },
    )

    val byName = enchants.map {
        val eval = it(EmptyItem())
        eval.displayName to { item: Item -> it(item) }
    }.toMap()
    val byId = enchants.map { val eval = it(EmptyItem()); eval.id to { item: Item -> it(item) } }.toMap()
    val bySlot = enchants.groupBy { val eval = it(EmptyItem()); eval.inventorySlot }.mapValues { it.value.toTypedArray() }
}
