package character.classes.hunter.pet

import character.*
import character.classes.hunter.pet.abilities.ClawRank9
import character.classes.hunter.pet.abilities.GoreRank9
import character.classes.hunter.pet.abilities.LightningBreathRank6
import character.classes.hunter.pet.buffs.*
import data.model.Item

// petDamageMultiplier is the +10% or -10% or whatever modifier the specific pet type gets
abstract class HunterPet(override var baseStats: Stats=Stats(), petDamageMultiplier: Double) : Class(mapOf()) {
    override fun talentFromString(name: String, ranks: Int): Talent? {
        return null
    }

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            "Claw" -> ClawRank9()
            "Gore" -> GoreRank9()
            "Lightning Breath" -> LightningBreathRank6()
            else -> null
        }
    }

    override var buffs: List<Buff> = listOf(
        PetBaseDamage(petDamageMultiplier),
        PetFocusRegen(),
        PetFrenzy(),
        PetFerociousInspiration(),
        PetFerocity(),
        PetSerpentsSwiftness(),
        PetAnimalHandler()
    )

    override var resourceType: Resource.Type = Resource.Type.FOCUS
    override var canDualWield: Boolean = false
    override var attackPowerFromAgility: Int = 0
    override var attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override val dodgePctPerAgility: Double = 1.0 / 25.0
    override val baseDodgePct: Double = 0.0
    override var rangedAttackPowerFromAgility: Int = 0

    override val baseMana: Int = 0
    override val baseSpellCritChance: Double = 0.0
}
