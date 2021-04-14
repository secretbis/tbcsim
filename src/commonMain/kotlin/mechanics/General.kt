package mechanics

import mu.KotlinLogging
import sim.SimParticipant
import kotlin.js.JsExport
import kotlin.math.sqrt

@JsExport
object General {
    private val logger = KotlinLogging.logger {}

    // Base mitigation values based on level difference
    val baseMissChance = mapOf(
        0 to 0.05,
        1 to 0.055,
        2 to 0.06,
        3 to 0.09
    )
    // TODO: Does TBC still have this?
    val critSuppression = mapOf(
        0 to 0.00,
        1 to 0.01,
        2 to 0.02,
        3 to 0.03
    )

    fun <T> valueByLevelDiff(sp: SimParticipant, table: Map<Int, T>) : T {
        val levelDiff = sp.sim.target.character.level - sp.character.level

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

    fun physicalBlockChance(sp: SimParticipant): Double {
        return if(sp.sim.opts.allowParryAndBlock) {
            // Mobs cannot block more than 5% of the time
            // https://github.com/magey/classic-warrior/wiki/Attack-table#block
            0.05
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
        val targetArmor = (sp.sim.target.armor() - sp.armorPen()).coerceAtLeast(0)
        return targetArmor / (targetArmor + (467.5 * sp.sim.target.character.level - 22167.5))
    }
}
