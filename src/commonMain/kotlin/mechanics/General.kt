package mechanics

import data.Constants
import data.model.Item
import mu.KotlinLogging
import sim.SimParticipant
import kotlin.js.JsExport
import kotlin.math.sqrt

@JsExport
object General {
    private val logger = KotlinLogging.logger {}

    // Base mitigation values based on level difference
    val baseMissChance: Double = 0.05
    val baseBlockChance: Double = 0.05

    fun <T> valueByLevelDiff(sp: SimParticipant, table: Map<Int, T>) : T {
        val levelDiff = sp.target().character.level - sp.character.level

        return when {
            levelDiff <= 0 -> {
                logger.warn { "Attempted to compute a hit on a target more than 3 levels below" }
                table[0]!!
            }
            levelDiff > 3 -> {
                logger.warn { "Attempted to compute a hit on a target more than 3 levels above" }
                table[3]!!
            }
            else -> table[levelDiff]!!
        }
    }

    fun dpsToAp(dps: Double): Int {
        return (14.0 * dps).toInt()
    }

    // The difference between attacker weapon skill (level * 5) and the target's defense skill
    fun skillDiff(sp: SimParticipant): Int {
        return ((sp.target().character.level * 5) + sp.target().defenseSkill()) - (sp.character.level * 5)
    }

    fun levelDiff(sp: SimParticipant): Int {
        return sp.target().character.level - sp.character.level
    }

    // Calculates the defense 0.04% per delta point to miss, dodge, block, parry - positive or negative
    fun defenseChance(sp: SimParticipant): Double {
        return 0.0004 * skillDiff(sp)
    }

    fun critSuppression(sp: SimParticipant): Double {
        return if(levelDiff(sp) >= 3) 0.018 else 0.0
    }

    fun baseMiss(sp: SimParticipant): Double {
        val levelDiff = levelDiff(sp)
        return if(sp.target().isBoss()) {
            if(levelDiff > 2) {
                val suppression = (levelDiff - 2) * 0.01
                baseMissChance + (levelDiff * 0.01) + suppression
            } else {
                baseMissChance + (levelDiff * 0.005)
            }
        } else {
            baseMissChance + (levelDiff * 0.01) + defenseChance(sp)
        }
    }

    fun baseCrit(sp: SimParticipant, item: Item?): Double {
        val itemBonusCritPct = if(item != null) {
            when {
                item.isGun() -> sp.stats.gunCritRating
                item.isBow() -> sp.stats.bowCritRating
                item.isCrossbow() -> sp.stats.bowCritRating
                else -> 0.0
            } / Rating.critPerPct
        } else 0.0

        val skillDiff = skillDiff(sp)
        val baseCrit = if(sp.target().isBoss()) {
            val levelDiff = levelDiff(sp)
            val levelDiffCrit = if(skillDiff < 0) {
                (sp.meleeCritPct() / 100) + (levelDiff * 0.01)
            } else {
                (sp.meleeCritPct() / 100) + (levelDiff * 0.002)
            }
            val suppression = critSuppression(sp)
            levelDiffCrit - suppression
        } else {
            (sp.meleeCritPct() / 100) - defenseChance(sp) - (sp.target().resiliencePct() / 100)
        }

        return (baseCrit + (itemBonusCritPct / 100)).coerceAtLeast(0.0)
    }

    fun physicalBlockChance(sp: SimParticipant): Double {
        return if(sp.sim.opts.allowParryAndBlock) {
            if(sp.target().isBoss()) {
                // Mobs cannot block more than 5% of the time
                // https://github.com/magey/classic-warrior/wiki/Attack-table#block

                (baseBlockChance + (levelDiff(sp) * -0.005)).coerceAtMost(baseBlockChance)
            } else {
                baseBlockChance + (sp.target().blockPct() / 100) + defenseChance(sp)
            }
        } else {
            0.0
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun physicalBlockReduction(sp: SimParticipant): Double {
        // TODO: How much is mitigated by a mob?
        // This is 46 for now since that's what Thaddius blocks for
        return 46.0
    }

    // This takes a list of *reductions* not multipliers, i.e. if a spell says reduced by 60%, send 0.6, not 0.4
    fun resourceCostReduction(baseCost: Double, reductions: List<Double>) : Double {
        // Resource reductions all work relative to the base mana cost, so each needs to be subtracted individually
        return reductions.fold(baseCost) { acc, reduction ->
            acc - acc * reduction
        }.coerceAtLeast(0.0)
    }

    fun mp5FromSpiritNotCasting(sp: SimParticipant): Int {
        val regen = 0.001 + (sp.spirit() * sqrt(sp.intellect().toDouble()) * 0.005596) * 5
        return regen.toInt()
    }

    fun physicalArmorMitigation(sp: SimParticipant): Double {
        val targetArmor = (sp.target().armor() - sp.armorPen()).coerceAtLeast(0)
        return targetArmor / (targetArmor + (467.5 * sp.target().character.level - 22167.5))
    }
}
