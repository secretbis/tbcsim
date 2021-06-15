package character.classes.priest

import character.*
import character.classes.priest.abilities.*
import character.classes.priest.talents.*
import character.classes.priest.abilities.Shadowfiend as Shadowfiend
import character.classes.priest.talents.PowerInfusion as PowerInfusionTalent
import character.classes.priest.talents.InnerFocus as InnerFocusTalent
import data.model.Item

class Priest(talents: Map<String, Talent>, spec: Spec) : Class(talents, spec) {
    override val baseStats: Stats = Stats(
        agility = 184,
        intellect = 180,
        strength = 146,
        stamina = 154,
        spirit = 135
    )
    override val buffs: List<Buff> = listOf()

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            Smite.name -> Smite()
            ShadowWordPain.name -> ShadowWordPain()
            Shadowfiend.name -> Shadowfiend()
            PowerInfusion.name -> PowerInfusion()
            InnerFocus.name -> InnerFocus()
            else -> null
        }
    }

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return when(name) {
            DivineFury.name -> DivineFury(ranks)
            FocusedPower.name -> FocusedPower(ranks)
            ForceOfWill.name -> ForceOfWill(ranks)
            HolySpecialization.name -> HolySpecialization(ranks)
            InnerFocusTalent.name -> InnerFocusTalent(ranks)
            Meditation.name -> Meditation(ranks)
            MentalAgility.name -> MentalAgility(ranks)
            MentalStrength.name -> MentalStrength(ranks)
            PowerInfusionTalent.name -> PowerInfusionTalent(ranks)
            SearingLight.name -> SearingLight(ranks)
            SpiritOfRedemption.name -> SpiritOfRedemption(ranks)
            SpiritualGuidance.name -> SpiritualGuidance(ranks)
            SurgeOfLight.name -> SurgeOfLight(ranks)
            else -> null
        }
    }

    override val resourceTypes: List<Resource.Type> = listOf(Resource.Type.MANA)
    override val canDualWield: Boolean = false
    override val attackPowerFromAgility: Int = 0
    override val attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override val rangedAttackPowerFromAgility: Int = 0
    override val baseMana: Int = 2620
    override val baseSpellCritChance: Double = 0.19
    override val dodgePctPerAgility: Double = 1.0 / 25.0
    override val baseDodgePct: Double = 3.18
}
