package character

import character.classes.shaman.Shaman
import character.classes.warlock.Warlock
import character.classes.warrior.Warrior
import data.model.Item

abstract class Class(
    var talents: Map<String, Talent>
) {
    companion object {
        fun fromString(name: String, talents: Map<String, Talent> = mapOf()): Class? {
            return when(name.toLowerCase().trim()) {
                "shaman" -> Shaman(talents)
                "warlock" -> Warlock(talents)
                "warrior" -> Warrior(talents)
                else -> null
            }
        }
    }

    // Everything that comes with the class
    abstract val baseStats: Stats
    abstract val buffs: List<Buff>

    // Ability factory
    abstract fun abilityFromString(name: String, item: Item? = null): Ability?

    // Talent factory
    abstract fun talentFromString(name: String, ranks: Int): Talent?

    // Class resource
    abstract val resourceType: Resource.Type

    abstract val canDualWield: Boolean

    abstract val attackPowerFromAgility: Int
    abstract val attackPowerFromStrength: Int
    abstract val critPctPerAgility: Double
    abstract val rangedAttackPowerFromAgility: Int
    abstract val dodgePctPerAgility: Double
    abstract val baseDodgePct: Double

    // Class-specific constant
    // Druid 2370
    // Hunter 3383
    // Mage 2241
    // Paladin 2953
    // Priest 2620
    // Shaman 2958
    // Warlock 2871
    abstract val baseMana: Int

    // Class-specific constant base spell crit
    // Druid 1.85
    // Mage 0.91
    // Paladin 3.336
    // Priest 1.24
    // Shaman 2.2
    // Warlock 1.701
    abstract val baseSpellCritChance: Double
}
