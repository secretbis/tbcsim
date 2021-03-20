package character.classes.warrior

import character.*
import character.classes.shaman.talents.Cruelty
import character.classes.shaman.talents.Flurry
import character.classes.warrior.abilities.*
import character.classes.warrior.abilities.Bloodthirst
import character.classes.warrior.abilities.DeathWish
import character.classes.warrior.abilities.MortalStrike
import character.classes.warrior.abilities.Rampage
import character.classes.warrior.buffs.RageGeneration
import character.classes.warrior.buffs.RampageBase
import character.classes.warrior.talents.*
import character.classes.warrior.talents.Bloodthirst as BloodthirstTalent
import character.classes.warrior.talents.DeathWish as DeathWishTalent
import character.classes.warrior.talents.MortalStrike as MortalStrikeTalent
import character.classes.warrior.talents.Rampage as RampageTalent
import data.model.Item

class Warrior(talents: Map<String, Talent>) : Class(talents) {
    override val baseStats: Stats = Stats(
        agility = 154,
        intellect = 123,
        strength = 252,
        stamina = 154,
        spirit = 117
    )

    override val buffs: List<Buff> = listOf(
        RageGeneration(),
        RampageBase()
    )

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            BattleShout.name -> BattleShout()
            Bloodrage.name -> Bloodrage()
            Bloodthirst.name -> Bloodthirst()
            DeathWish.name -> DeathWish()
            Execute.name -> Execute()
            HeroicStrike.name -> HeroicStrike()
            MortalStrike.name -> MortalStrike()
            Rampage.name -> Rampage()
            Recklessness.name -> Recklessness()
            Slam.name -> Slam()
            Whirlwind.name -> Whirlwind()
            else -> null
        }
    }

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return when(name) {
            AngerManagement.name -> AngerManagement(ranks)
            BloodFrenzy.name -> BloodFrenzy(ranks)
            BloodthirstTalent.name -> BloodthirstTalent(ranks)
            CommandingPresence.name -> CommandingPresence(ranks)
            Cruelty.name -> Cruelty(ranks)
            DeathWishTalent.name -> DeathWishTalent(ranks)
            DeepWounds.name -> DeepWounds(ranks)
            DualWieldSpec.name -> DualWieldSpec(ranks)
            EndlessRage.name -> EndlessRage(ranks)
            Flurry.name -> Flurry(ranks)
            Impale.name -> Impale(ranks)
            ImprovedBerserkerStance.name -> ImprovedBerserkerStance(ranks)
            ImprovedExecute.name -> ImprovedExecute(ranks)
            ImprovedHeroicStrike.name -> ImprovedHeroicStrike(ranks)
            ImprovedMortalStrike.name -> ImprovedMortalStrike(ranks)
            ImprovedSlam.name -> ImprovedSlam(ranks)
            ImprovedWhirlwind.name -> ImprovedWhirlwind(ranks)
            MaceSpec.name -> MaceSpec(ranks)
            MortalStrikeTalent.name -> MortalStrikeTalent(ranks)
            PoleaxeSpec.name -> PoleaxeSpec(ranks)
            Precision.name -> Precision(ranks)
            RampageTalent.name -> RampageTalent(ranks)
            SweepingStrikes.name -> SweepingStrikes(ranks)
            SwordSpec.name -> SwordSpec(ranks)
            TacticalMastery.name -> TacticalMastery(ranks)
            TwoHandWeaponSpec.name -> TwoHandWeaponSpec(ranks)
            UnbridledWrath.name -> UnbridledWrath(ranks)
            WeaponMastery.name -> WeaponMastery(ranks)
            else -> null
        }
    }

    override val resourceType: Resource.Type = Resource.Type.RAGE
    override val canDualWield: Boolean = true
    override val attackPowerFromAgility: Int = 0
    override val attackPowerFromStrength: Int = 2
    override val critPctPerAgility: Double = 1.0 / 33.0
    override val dodgePctPerAgility: Double = 1.0 / 30.0
    override val baseDodgePct: Double = 0.75
    override val rangedAttackPowerFromAgility: Int = 0
    override val baseMana: Int = 0
    override val baseSpellCritChance: Double = 0.0
}
