package data.buffs

import character.Buff
import data.buffs.permanent.*
import data.model.Item
import mu.KotlinLogging

object Buffs {
    private val logger = KotlinLogging.logger {}

    private val mappersByName = listOf(
        Regex("Armor Penetration \\d+") to fun (name: String): Buff {
            val amount = name.drop(18).toInt()
            return GenericArmorPenBuff(amount)
        },
        Regex("Attack Power \\d+") to fun (name: String): Buff {
            val amount = name.drop(13).toInt()
            return GenericAttackPowerBuff(amount)
        },
        Regex("Attack Power Ranged \\d+") to fun (name: String): Buff {
            val amount = name.drop(20).toInt()
            return GenericRangedAttackPowerBuff(amount)
        },
        Regex("Increase Healing \\d+") to fun (name: String): Buff {
            val amount = name.drop(17).toInt()
            return GenericSpellHealingBuff(amount)
        },
        Regex("Increase Spell Dam \\d+") to fun (name: String): Buff {
            val amount = name.drop(19).toInt()
            return GenericSpellDamageBuff(amount)
        },
        Regex("Block Value \\d+") to fun (name: String): Buff {
            val amount = name.drop(12).toInt()
            return GenericBlockValueBuff(amount)
        },
        Regex("Increase Holy Dam \\d+") to fun (name: String): Buff {
            val amount = name.drop(18).toInt()
            return GenericHolyDamageBuff(amount)
        },
        Regex("Increase Fire Dam \\d+") to fun (name: String): Buff {
            val amount = name.drop(18).toInt()
            return GenericFireDamageBuff(amount)
        },
        Regex("Increase Nature Dam \\d+") to fun (name: String): Buff {
            val amount = name.drop(20).toInt()
            return GenericNatureDamageBuff(amount)
        },
        Regex("Increase Frost Dam \\d+") to fun (name: String): Buff {
            val amount = name.drop(19).toInt()
            return GenericFrostDamageBuff(amount)
        },
        Regex("Increase Shadow Dam \\d+") to fun (name: String): Buff {
            val amount = name.drop(20).toInt()
            return GenericShadowDamageBuff(amount)
        },
        Regex("Increase Arcane Dam \\d+") to fun (name: String): Buff {
            val amount = name.drop(20).toInt()
            return GenericArcaneDamageBuff(amount)
        },
        Regex("Increased Dodge \\d+") to fun (name: String): Buff {
            val amount = name.drop(16).toInt()
            return GenericDodgeRatingBuff(amount)
        },
        Regex("Increased Hit Rating \\d+") to fun (name: String): Buff {
            val amount = name.drop(21).toInt()
            return GenericHitRatingBuff(amount)
        },
        Regex("Increased Critical \\d+") to fun (name: String): Buff {
            val amount = name.drop(19).toInt()
            return GenericCritRatingBuff(amount)
        },
        Regex("Increased Spell Hit Chance \\d+") to fun (name: String): Buff {
            val amount = name.drop(27).toInt()
            return GenericSpellHitRatingBuff(amount)
        },
        Regex("Spell Penetration \\d+") to fun (name: String): Buff {
            val amount = name.drop(18).toInt()
            return GenericSpellPenBuff(amount)
        },
        Regex("Increased Spell Penetration \\d+") to fun (name: String): Buff {
            val amount = name.drop(28).toInt()
            return GenericSpellPenBuff(amount)
        },
        Regex("Attack Power - Feral \\(\\+\\d+\\)") to fun (name: String): Buff {
            val amount = name.drop(21).dropLast(1).toInt()
            return GenericFeralAttackPowerBuff(amount)
        }
    )

    fun byIdOrName(id: Int, name: String, item: Item): Buff? {
        val byIdOrName = byId(id, item) ?: mappersByName.find {it.first.matches(name.trim()) }?.second?.invoke(name.trim())

        if(byIdOrName == null) {
            logger.warn { "Unable to resolve buff: $id - $name" }
        }

        return byIdOrName
    }

    fun byId(id: Int, item: Item): Buff? {
        return when(id) {
            21165 -> BSHammerHaste(item)
            33297 -> QuagmirransEye()
            33648 -> RageOfTheUnraveller()
            34244 -> TotemOfTheAstralWinds()
            34774 -> DragonspineTrophy()
            35166 -> BloodlustBrooch()
            39438 -> DarkmoonCardCrusadeAP()
            39440 -> DarkmoonCardCrusadeSP()

            // All the random mp5 buffs
            18378 -> GenericManaRegenBuff(8)
            18379 -> GenericManaRegenBuff(6)
            20959 -> GenericManaRegenBuff(10)
            21359 -> GenericManaRegenBuff(1)
            21360 -> GenericManaRegenBuff(2)
            21361 -> GenericManaRegenBuff(3)
            21362 -> GenericManaRegenBuff(4)
            21363 -> GenericManaRegenBuff(5)
            21364 -> GenericManaRegenBuff(7)
            21365 -> GenericManaRegenBuff(9)
            21366 -> GenericManaRegenBuff(11)
            21618 -> GenericManaRegenBuff(4)
            21619 -> GenericManaRegenBuff(4)
            21620 -> GenericManaRegenBuff(5)
            21621 -> GenericManaRegenBuff(1)
            21622 -> GenericManaRegenBuff(1)
            21623 -> GenericManaRegenBuff(2)
            21624 -> GenericManaRegenBuff(2)
            21625 -> GenericManaRegenBuff(3)
            21626 -> GenericManaRegenBuff(6)
            21627 -> GenericManaRegenBuff(6)
            21628 -> GenericManaRegenBuff(7)
            21629 -> GenericManaRegenBuff(8)
            21630 -> GenericManaRegenBuff(8)
            21631 -> GenericManaRegenBuff(9)
            21632 -> GenericManaRegenBuff(10)
            21633 -> GenericManaRegenBuff(10)
            21634 -> GenericManaRegenBuff(11)
            21635 -> GenericManaRegenBuff(12)
            21636 -> GenericManaRegenBuff(12)
            21637 -> GenericManaRegenBuff(12)
            21638 -> GenericManaRegenBuff(13)
            21639 -> GenericManaRegenBuff(13)
            21640 -> GenericManaRegenBuff(14)
            21641 -> GenericManaRegenBuff(14)
            21642 -> GenericManaRegenBuff(14)
            21643 -> GenericManaRegenBuff(15)
            21644 -> GenericManaRegenBuff(15)
            35836 -> GenericManaRegenBuff(16)

            else -> null
        }
    }
}
