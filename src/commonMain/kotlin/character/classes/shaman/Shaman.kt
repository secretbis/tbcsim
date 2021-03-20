package character.classes.shaman

import character.*
import character.classes.shaman.abilities.*
import character.classes.shaman.talents.*
import character.classes.shaman.talents.ElementalMastery as ElementalMasteryTalent
import character.classes.shaman.talents.ShamanisticRage as ShamanisticRageTalent
import character.classes.shaman.talents.Stormstrike as StormstrikeTalent
import character.classes.shaman.talents.TotemOfWrath as TotemOfWrathTalent
import data.model.Item

class Shaman(talents: Map<String, Talent>) : Class(talents) {
    override fun talentFromString(name: String, ranks: Int): Talent? {
        return when(name.trim()) {
            AncestralKnowledge.name -> AncestralKnowledge(ranks)
            CallOfThunder.name -> CallOfThunder(ranks)
            Concussion.name -> Concussion(ranks)
            Convection.name -> Convection(ranks)
            DualWield.name -> DualWield(ranks)
            DualWieldSpecialization.name -> DualWieldSpecialization(ranks)
            ElementalDevastation.name -> ElementalDevastation(ranks)
            ElementalFocus.name -> ElementalFocus(ranks)
            ElementalFury.name -> ElementalFury(ranks)
            ElementalMasteryTalent.name -> ElementalMasteryTalent(ranks)
            ElementalPrecision.name -> ElementalPrecision(ranks)
            ElementalWeapons.name -> ElementalWeapons(ranks)
            EnhancingTotems.name -> EnhancingTotems(ranks)
            Flurry.name -> Flurry(ranks)
            ImprovedWeaponTotems.name -> ImprovedWeaponTotems(ranks)
            LightningMastery.name -> LightningMastery(ranks)
            LightningOverload.name -> LightningOverload(ranks)
            MentalQuickness.name -> MentalQuickness(ranks)
            NaturesGuidance.name -> NaturesGuidance(ranks)
            Reverberation.name -> Reverberation(ranks)
            ShamanisticFocus.name -> ShamanisticFocus(ranks)
            ShamanisticRageTalent.name -> ShamanisticRageTalent(ranks)
            StormstrikeTalent.name -> StormstrikeTalent(ranks)
            ThunderingStrikes.name -> ThunderingStrikes(ranks)
            TidalMastery.name -> TidalMastery(ranks)
            TotemicFocus.name -> TotemicFocus(ranks)
            TotemOfWrathTalent.name -> TotemOfWrathTalent(ranks)
            UnleashedRage.name -> UnleashedRage(ranks)
            UnrelentingStorm.name -> UnrelentingStorm(ranks)
            WeaponMastery.name -> WeaponMastery(ranks)
            else -> null
        }
    }

    override fun abilityFromString(name: String, item: Item?): Ability?{
        return when(name) {
            ChainLightning.name -> ChainLightning()
            EarthShock.name -> EarthShock()
            ElementalMastery.name -> ElementalMastery()
            FlameShock.name -> FlameShock()
            FlametongueWeaponMainHand.name -> FlametongueWeaponMainHand()
            FlametongueWeaponOffHand.name -> FlametongueWeaponOffHand()
            GraceOfAirTotem.name -> GraceOfAirTotem()
            LightningBolt.name -> LightningBolt()
            ManaSpringTotem.name -> ManaSpringTotem()
            Stormstrike.name -> Stormstrike()
            StrengthOfEarthTotem.name -> StrengthOfEarthTotem()
            ShamanisticRage.name -> ShamanisticRage()
            TotemOfWrath.name -> TotemOfWrath()
            WaterShield.name -> WaterShield()
            "Windfury Totem" -> WindfuryTotem()
            "Windfury Totem (Rank 1)" -> WindfuryTotemRank1()
            WindfuryWeaponMainHand.name -> WindfuryWeaponMainHand()
            WindfuryWeaponOffHand.name -> WindfuryWeaponOffHand()
            WrathOfAirTotem.name -> WrathOfAirTotem()
            else -> null
        }
    }

    override var baseStats: Stats = Stats(
        agility = 222,
        intellect = 180,
        strength = 108,
        stamina = 154,
        spirit = 135
    )

    override val buffs: List<Buff> = listOf()

    override val resourceType: Resource.Type = Resource.Type.MANA

    override val canDualWield: Boolean
        get() = talents["Dual Wield"]?.currentRank == 1

    override val attackPowerFromAgility: Int = 0
    override val attackPowerFromStrength: Int = 2
    override val critPctPerAgility: Double = 1.0 / 25.0
    override val dodgePctPerAgility: Double = 1.0 / 25.0
    override val baseDodgePct: Double = 1.67
    override val rangedAttackPowerFromAgility: Int = 1

    override val baseMana: Int = 2958
    override val baseSpellCritChance: Double = 2.2
}
