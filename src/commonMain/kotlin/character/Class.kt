package character

import character.classes.hunter.Hunter
import character.classes.hunter.specs.*
import character.classes.mage.Mage
import character.classes.mage.specs.Arcane
import character.classes.mage.specs.Fire
import character.classes.mage.specs.Frost
import character.classes.priest.Priest
import character.classes.priest.specs.Shadow
import character.classes.shaman.Shaman
import character.classes.shaman.specs.Elemental
import character.classes.shaman.specs.Enhancement
import character.classes.warlock.Warlock
import character.classes.warlock.specs.Affliction
import character.classes.warlock.specs.Destruction
import character.classes.warrior.Warrior
import character.classes.warrior.specs.Arms
import character.classes.warrior.specs.Fury
import character.classes.rogue.specs.Combat
import character.classes.rogue.Rogue
import character.classes.rogue.specs.Assassination
import character.classes.warrior.specs.Kebab
import character.classes.warrior.specs.Protection
import data.model.Item

abstract class Class(
    var talents: Map<String, Talent>,
    val spec: Spec
) {
    companion object {
        fun fromString(name: String, _specName: String, talents: Map<String, Talent> = mapOf()): Class? {
            val className = name.toLowerCase().trim()
            val specName = _specName.toLowerCase().trim()

            val spec = specFromString(className, specName) ?: throw Exception("Invalid class + spec: $className - $specName")
            return when(className) {
                "hunter" -> Hunter(talents, spec)
                "mage" -> Mage(talents, spec)
                "priest" -> Priest(talents, spec)
                "shaman" -> Shaman(talents, spec)
                "warlock" -> Warlock(talents, spec)
                "warrior" -> Warrior(talents, spec)
                "rogue" -> Rogue(talents, spec)
                else -> null
            }
        }

        fun specFromString(_className: String, _specName: String): Spec? {
            val className = _className.toLowerCase().trim()
            val specName = _specName.toLowerCase().trim()

            return when(className) {
                "hunter" -> when(specName) {
                    "beast mastery" -> BeastMastery()
                    "marksmanship" -> Marksmanship()
                    "survival" -> Survival()
                    else -> null
                }
                "mage" -> when(specName) {
                    "arcane" -> Arcane()
                    "fire" -> Fire()
                    "frost" -> Frost()
                    else -> null
                }
                "priest" -> when(specName) {
                    "shadow" -> Shadow()
                    else -> null
                }
                "shaman" -> when(specName) {
                    "enhancement" -> Enhancement()
                    "elemental" -> Elemental()
                    else -> null
                }
                "warlock" -> when(specName) {
                    "affliction" -> Affliction()
                    "destruction" -> Destruction()
                    else -> null
                }
                "warrior" -> when(specName) {
                    "arms" -> Arms()
                    "fury" -> Fury()
                    "kebab" -> Kebab()
                    "protection" -> Protection()
                    else -> null
                }
                "rogue" -> when(specName) {
                    "assassination" -> Assassination()
                    "combat" -> Combat()
                    else -> null
                }
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
    abstract val resourceTypes: List<Resource.Type>

    abstract val canDualWield: Boolean

    abstract val attackPowerFromAgility: Int
    abstract val attackPowerFromStrength: Int
    abstract val critPctPerAgility: Double
    abstract val rangedAttackPowerFromAgility: Int
    abstract val dodgePctPerAgility: Double
    abstract val baseDodgePct: Double
    // https://wow.gamepedia.com/Spell_critical_strike
    open val spellCritPctPerInt: Double = 1.0 / 80.0

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

    open fun <T : Talent> talentInstance(name: String): T? {
        return talents[name] as? T?
    }

    open fun talentRanks(name: String): Int {
        return talents[name]?.currentRank ?: 0
    }

    open fun hasTalentRanks(name: String, minRanks: Int = 1): Boolean {
        return talentRanks(name) >= minRanks
    }
}
