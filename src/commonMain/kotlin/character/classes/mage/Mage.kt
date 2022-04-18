package character.classes.mage

import character.*
import character.classes.mage.abilities.*
import character.classes.mage.talents.*
import data.abilities.generic.ManaEmerald
import character.classes.mage.talents.ArcanePower as ArcanePowerTalent
import character.classes.mage.talents.ColdSnap as ColdSnapTalent
import character.classes.mage.talents.Combustion as CombustionTalent
import character.classes.mage.talents.IcyVeins as IcyVeinsTalent
import character.classes.mage.talents.PresenceOfMind as PresenceOfMindTalent
import character.classes.mage.talents.SummonWaterElemental as SummonWaterElementalTalent
import data.model.Item

class Mage(talents: Map<String, Talent>, spec: Spec) : Class(talents, spec) {
    override val baseStats: Stats = Stats(
        agility = 154,
        intellect = 180,
        strength = 112,
        stamina = 154,
        spirit = 199
    )
    override val buffs: List<Buff> = listOf()

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            ArcaneBlast.name -> ArcaneBlast()
            ArcaneIntellect.name -> ArcaneIntellect()
            ArcanePower.name -> ArcanePower()
            ColdSnap.name -> ColdSnap()
            Combustion.name -> Combustion()
            Evocation.name -> Evocation()
            Fireball.name -> Fireball()
            FireBlast.name -> FireBlast()
            Frostbolt.name -> Frostbolt()
            IcyVeins.name -> IcyVeins()
            ManaEmerald.name -> ManaEmerald()
            MoltenArmor.name -> MoltenArmor()
            MageArmor.name -> MageArmor()
            PresenceOfMind.name -> PresenceOfMind()
            Scorch.name -> Scorch()
            SummonWaterElemental.name -> SummonWaterElemental()
            else -> null
        }
    }

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return when(name) {
            ArcaneConcentration.name -> ArcaneConcentration(ranks)
            ArcaneFocus.name -> ArcaneFocus(ranks)
            ArcaneImpact.name -> ArcaneImpact(ranks)
            ArcaneInstability.name -> ArcaneInstability(ranks)
            ArcaneMeditation.name -> ArcaneMeditation(ranks)
            ArcaneMind.name -> ArcaneMind(ranks)
            ArcanePotency.name -> ArcanePotency(ranks)
            ArcanePowerTalent.name -> ArcanePowerTalent(ranks)
            ArcticWinds.name -> ArcticWinds(ranks)
            BlastWave.name -> BlastWave(ranks)
            ColdSnapTalent.name -> ColdSnapTalent(ranks)
            CombustionTalent.name -> CombustionTalent(ranks)
            CriticalMass.name -> CriticalMass(ranks)
            DragonsBreath.name -> DragonsBreath(ranks)
            ElementalPrecision.name -> ElementalPrecision(ranks)
            EmpoweredArcaneMissiles.name -> EmpoweredArcaneMissiles(ranks)
            EmpoweredFireball.name -> EmpoweredFireball(ranks)
            EmpoweredFrostbolt.name -> EmpoweredFrostbolt(ranks)
            FirePower.name -> FirePower(ranks)
            FrostChanneling.name -> FrostChanneling(ranks)
            IceFloes.name -> IceFloes(ranks)
            IceShards.name -> IceShards(ranks)
            IcyVeinsTalent.name -> IcyVeinsTalent(ranks)
            Ignite.name -> Ignite(ranks)
            ImprovedConeOfCold.name -> ImprovedConeOfCold(ranks)
            ImprovedFireball.name -> ImprovedFireball(ranks)
            ImprovedFireBlast.name -> ImprovedFireBlast(ranks)
            ImprovedFlamestrike.name -> ImprovedFlamestrike(ranks)
            ImprovedFrostbolt.name -> ImprovedFrostbolt(ranks)
            ImprovedScorch.name -> ImprovedScorch(ranks)
            Incineration.name -> Incineration(ranks)
            MasterOfElements.name -> MasterOfElements(ranks)
            MindMastery.name -> MindMastery(ranks)
            MoltenFury.name -> MoltenFury(ranks)
            PiercingIce.name -> PiercingIce(ranks)
            PlayingWithFire.name -> PlayingWithFire(ranks)
            PresenceOfMindTalent.name -> PresenceOfMindTalent(ranks)
            Pyroblast.name -> Pyroblast(ranks)
            Pyromaniac.name -> Pyromaniac(ranks)
            SpellPower.name -> SpellPower(ranks)
            SummonWaterElementalTalent.name -> SummonWaterElementalTalent(ranks)
            WintersChill.name -> WintersChill(ranks)
            else -> null
        }
    }

    override val resourceTypes: List<Resource.Type> = listOf(Resource.Type.MANA)
    override val canDualWield: Boolean = false
    override val attackPowerFromAgility: Int = 0
    override val attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override val rangedAttackPowerFromAgility: Int = 0
    override val baseMana: Int = 2241
    override val baseSpellCritChance: Double = 0.91
    override val dodgePctPerAgility: Double = 1.0 / 25.0
    override val baseDodgePct: Double = 3.45
}
