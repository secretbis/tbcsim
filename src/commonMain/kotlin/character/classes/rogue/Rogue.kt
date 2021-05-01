package character.classes.rogue

import character.*
import data.model.Item
import character.classes.rogue.abilities.*
import character.classes.rogue.talents.*
import character.classes.rogue.buffs.*

class Rogue(talents: Map<String, Talent>, spec: Spec) : Class(talents, spec) {
    override val baseStats: Stats = Stats(
        agility = 222,
        intellect = 123,
        strength = 209,
        stamina = 154,
        spirit = 92
    )
    override val buffs: List<Buff> = listOf(
        BaseEnergyGeneneration(),
        Stealth(), // start stealthed
    )

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            SinisterStrike.name -> SinisterStrike()
            else -> null
        }
    }

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return when(name) {
            CombatPotency.name -> CombatPotency(ranks)
            else -> null
        }
    }

    override val resourceTypes: List<Resource.Type> = listOf(Resource.Type.ENERGY, Resource.Type.COMBO_POINT)
    override val canDualWield: Boolean = true
    override val attackPowerFromAgility: Int = 1
    override val attackPowerFromStrength: Int = 1
    override val critPctPerAgility: Double = 1.0 / 40
    override val rangedAttackPowerFromAgility: Int = 1
    override val baseMana: Int = 0
    override val baseSpellCritChance: Double = 0.0
    override val dodgePctPerAgility: Double = 1.0 / 20.0
    override val baseDodgePct: Double = -0.59
}
