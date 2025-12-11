package data

import kotlin.js.JsExport
import kotlin.js.JsName

// This contains meanings for all the magic numbers in the WoW TBC database
@JsExport
object Constants {
    const val UNKNOWN_ICON = "inv_misc_questionmark.jpg"

    enum class StatType(private val id: Int) {
        MANA(0),
        HEALTH(1),
        AGILITY(3),
        STRENGTH(4),
        INTELLECT(5),
        SPIRIT(6),
        STAMINA(7),
        DEFENSE_SKILL_RATING(12),
        DODGE_RATING(13),
        PARRY_RATING(14),
        BLOCK_RATING(15),
        HIT_MELEE_RATING(16),
        HIT_RANGED_RATING(17),
        HIT_SPELL_RATING(18),
        CRIT_MELEE_RATING(19),
        CRIT_RANGED_RATING(20),
        CRIT_SPELL_RATING(21),
        // These are never actually used as far as I can tell
//        HIT_TAKEN_MELEE_RATING(22),
//        HIT_TAKEN_RANGED_RATING(23),
//        HIT_TAKEN_SPELL_RATING(24),
//        CRIT_TAKEN_MELEE_RATING(25),
//        CRIT_TAKEN_RANGED_RATING(26),
//        CRIT_TAKEN_SPELL_RATING(27),
        HASTE_MELEE_RATING(28),
        HASTE_RANGED_RATING(29),
        HASTE_SPELL_RATING(30),
        HIT_RATING(31),
        CRIT_RATING(32),
        HIT_TAKEN_RATING(33),
        CRIT_TAKEN_RATING(34),
        RESILIENCE_RATING(35),
        HASTE_RATING(36),
        EXPERTISE_RATING(37),

        // These aren't actual DB values, and are likely implemented in other ways
        BLOCK_VALUE(93),
        SPELL_HEALING(94),
        ARMOR_PEN(95),
        SPELL_PEN(96),
        MANA_PER_5_SECONDS(97),
        SPELL_DAMAGE(98),
        ATTACK_POWER(99);

        operator fun invoke(): Int {
            return id
        }
    }

    // These values are bitmasks
    enum class AllowableClass(private val mask: Int) {
        WARRIOR(1),
        PALADIN(2),
        HUNTER(4),
        ROGUE(8),
        PRIEST(16),
        DEATHKNIGHT(32),
        SHAMAN(64),
        MAGE(128),
        WARLOCK(256),
        DRUID(1024);

        operator fun invoke(): Int {
            return mask
        }
    }

    // These values are ordinal
    enum class DamageType {
        PHYSICAL,
        PHYSICAL_IGNORE_ARMOR,
        HOLY,
        FIRE,
        NATURE,
        FROST,
        SHADOW,
        ARCANE;

        operator fun invoke(): Int {
            return ordinal
        }
    }

    // These values are ordinal
    enum class InventorySlot {
        NOT_EQUIPPABLE,
        HEAD,
        NECK,
        SHOULDER,
        SHIRT,
        CHEST,
        WAIST,
        LEGS,
        FEET,
        WRISTS,
        HANDS,
        FINGER,
        TRINKET,
        WEAPON,
        SHIELD,
        RANGED,
        BACK,
        TWO_HAND,
        BAG,
        TABARD,
        ROBE,
        MAIN_HAND,
        OFF_HAND,
        HOLDABLE_TOME,
        AMMO,
        THROWN,
        RANGED_RIGHT,
        QUIVER,
        RELIC;

        operator fun invoke(): Int {
            return ordinal
        }
    }

    // These values are ordinal
    enum class ItemClass {
        CONSUMABLE,
        CONTAINER,
        WEAPON,
        GEM,
        ARMOR,
        REAGENT,
        PROJECTILE,
        TRADE_GOODS,
        GENERIC_OBSOLETE,
        RECIPE,
        MONEY_OBSOLETE,
        QUIVER,
        QUEST,
        KEY,
        PERMANENT_OBSOLETE,
        MISCELLANEOUS,
        GLYPH;

        companion object {
            fun subclasses(itemClass: ItemClass?) : List<ItemSubclass> {
                if(itemClass == null) return listOf()
                return ItemSubclass.values().filter { it.itemClass == itemClass }
            }
        }

        operator fun invoke(): Int {
            return ordinal
        }
    }

    enum class ItemSubclass(val itemClass: ItemClass, val itemClassOrdinal: Int) {
        CONSUMABLE(ItemClass.CONSUMABLE,0),
        POTION(ItemClass.CONSUMABLE,1),
        ELIXIR(ItemClass.CONSUMABLE,2),
        FLASK(ItemClass.CONSUMABLE,3),
        SCROLL(ItemClass.CONSUMABLE,4),
        FOOD_DRINK(ItemClass.CONSUMABLE,5),
        ITEM_ENHANCEMENT(ItemClass.CONSUMABLE,6),
        BANDANGE(ItemClass.CONSUMABLE,7),
        OTHER(ItemClass.CONSUMABLE,8),

        BAG(ItemClass.CONTAINER,0),
        SOUL_BAG(ItemClass.CONTAINER,1),
        HERB_BAG(ItemClass.CONTAINER,2),
        ENCHANTING_BAG(ItemClass.CONTAINER,3),
        ENGINEERING_BAG(ItemClass.CONTAINER,4),
        GEM_BAG(ItemClass.CONTAINER,5),
        MINING_BAG(ItemClass.CONTAINER,6),
        LEATHERWORKING_BAG(ItemClass.CONTAINER,7),

        AXE_1H(ItemClass.WEAPON,0),
        AXE_2H(ItemClass.WEAPON,1),
        BOW(ItemClass.WEAPON,2),
        GUN(ItemClass.WEAPON,3),
        MACE_1H(ItemClass.WEAPON,4),
        MACE_2H(ItemClass.WEAPON,5),
        POLEARM(ItemClass.WEAPON,6),
        SWORD_1H(ItemClass.WEAPON,7),
        SWORD_2H(ItemClass.WEAPON,8),
        OBSOLETE(ItemClass.WEAPON,9),
        STAFF(ItemClass.WEAPON,10),
        EXOTIC_1(ItemClass.WEAPON,11),
        EXOTIC_2(ItemClass.WEAPON,12),
        FIST(ItemClass.WEAPON,13),
        MISC_TOOL(ItemClass.WEAPON,14),
        DAGGER(ItemClass.WEAPON,15),
        THROWN(ItemClass.WEAPON,16),
        SPEAR(ItemClass.WEAPON,17),
        CROSSBOW(ItemClass.WEAPON,18),
        WAND(ItemClass.WEAPON,19),
        FISHING_POLE(ItemClass.WEAPON,20),

        RED(ItemClass.GEM, 0),
        BLUE(ItemClass.GEM, 1),
        YELLOW(ItemClass.GEM, 2),
        PURPLE(ItemClass.GEM, 3),
        GREEN(ItemClass.GEM, 4),
        ORANGE(ItemClass.GEM, 5),
        META(ItemClass.GEM, 6),
        SIMPLE(ItemClass.GEM, 7),
        PRISMATIC(ItemClass.GEM, 8),

        MISC(ItemClass.ARMOR, 0),
        CLOTH(ItemClass.ARMOR, 1),
        LEATHER(ItemClass.ARMOR, 2),
        MAIL(ItemClass.ARMOR, 3),
        PLATE(ItemClass.ARMOR, 4),
        BUCKLER_OBSOLETE(ItemClass.ARMOR, 5),
        SHIELD(ItemClass.ARMOR, 6),
        LIBRAM(ItemClass.ARMOR, 7),
        IDOL(ItemClass.ARMOR, 8),
        TOTEM(ItemClass.ARMOR, 9),

        REAGENT(ItemClass.REAGENT, 0),

        WAND_OBSOLETE(ItemClass.PROJECTILE, 0),
        BOLT_OBSOLETE(ItemClass.PROJECTILE, 1),
        ARROW(ItemClass.PROJECTILE, 2),
        BULLET(ItemClass.PROJECTILE, 3),
        THROWN_OBSOLETE(ItemClass.PROJECTILE, 4),

        TRADE_GOODS(ItemClass.TRADE_GOODS, 0),
        PARTS(ItemClass.TRADE_GOODS, 1),
        EXPLOSIVES(ItemClass.TRADE_GOODS, 2),
        DEVICES(ItemClass.TRADE_GOODS, 3),
        TRADE_JEWELCRAFTING(ItemClass.TRADE_GOODS, 4),
        TRADE_CLOTH(ItemClass.TRADE_GOODS, 5),
        TRADE_LEATHER(ItemClass.TRADE_GOODS, 6),
        METAL_AND_STONE(ItemClass.TRADE_GOODS, 7),
        MEAT(ItemClass.TRADE_GOODS, 8),
        HERB(ItemClass.TRADE_GOODS, 9),
        ELEMENTAL(ItemClass.TRADE_GOODS, 10),
        TRADE_OTHER(ItemClass.TRADE_GOODS, 11),
        TRADE_ENCHANTING(ItemClass.TRADE_GOODS, 12),

        GENERIC_OBSOLETE(ItemClass.GENERIC_OBSOLETE, 0),

        BOOK(ItemClass.RECIPE, 0),
        LEATHERWORKING(ItemClass.RECIPE, 1),
        TAILORING(ItemClass.RECIPE, 2),
        ENGINEERING(ItemClass.RECIPE, 3),
        BLACKSMITHING(ItemClass.RECIPE, 4),
        COOKING(ItemClass.RECIPE, 5),
        ALCHEMY(ItemClass.RECIPE, 6),
        FIRST_AID(ItemClass.RECIPE, 7),
        ENCHANTING(ItemClass.RECIPE, 8),
        FISHING(ItemClass.RECIPE, 9),
        JEWELCRAFTING(ItemClass.RECIPE, 10),

        MONEY_OBSOLETE(ItemClass.MONEY_OBSOLETE, 0),

        QUIVER_OBSOLETE(ItemClass.QUIVER, 0),
        QUIVER_OBSOLETE_2(ItemClass.QUIVER, 1),
        QUIVER(ItemClass.QUIVER, 2),
        AMMO_POUCH(ItemClass.QUIVER, 3),

        QUEST(ItemClass.QUEST, 0),

        KEY(ItemClass.KEY, 0),
        LOCKPICK(ItemClass.KEY, 1),

        PERMANENT_OBSOLETE(ItemClass.PERMANENT_OBSOLETE, 0),

        JUNK(ItemClass.MISCELLANEOUS, 0),
        REAGENT_MISC(ItemClass.MISCELLANEOUS, 1),
        PET(ItemClass.MISCELLANEOUS, 2),
        HOLIDAY(ItemClass.MISCELLANEOUS, 3),
        OTHER_MISC(ItemClass.MISCELLANEOUS, 4),
        MOUNT(ItemClass.MISCELLANEOUS, 5),

        WARRIOR(ItemClass.GLYPH, 1),
        PALADIN(ItemClass.GLYPH, 2),
        HUNTER(ItemClass.GLYPH, 3),
        ROGUE(ItemClass.GLYPH, 4),
        PRIEST(ItemClass.GLYPH, 5),
        DEATH_KNIGHT(ItemClass.GLYPH, 6),
        SHAMAN(ItemClass.GLYPH, 7),
        MAGE(ItemClass.GLYPH, 8),
        WARLOCK(ItemClass.GLYPH, 9),
        DRUID(ItemClass.GLYPH, 11);

        operator fun invoke(): Int {
            return itemClassOrdinal
        }
    }

    // These values are ordinal
    enum class SpellTrigger {
        USE,
        ON_EQUIP,
        CHANCE_ON_HIT,
        SOULSTONE,
        USE_WITH_NO_DELAY;

        operator fun invoke(): Int {
            return ordinal
        }
    }
}
