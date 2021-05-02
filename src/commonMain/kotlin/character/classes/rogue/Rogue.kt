package character.classes.rogue

import character.*
import data.model.Item
import character.classes.rogue.abilities.*
import character.classes.rogue.talents.*
import character.classes.rogue.buffs.*

class Rogue(talents: Map<String, Talent>, spec: Spec) : Class(talents, spec) {
    override val baseStats: Stats = Stats(
        agility = 160 - 10,
        intellect = 35,
        strength = 96 - 10,
        stamina = 90,
        spirit = 59,
        attackPower = 140
    )
    override val buffs: List<Buff> = listOf(
        BaseEnergyGeneneration()
    )

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            character.classes.rogue.abilities.AdrenalineRush.name -> character.classes.rogue.abilities.AdrenalineRush()
            character.classes.rogue.abilities.Mutilate.name -> character.classes.rogue.abilities.Mutilate()
            character.classes.rogue.abilities.SliceAndDice.name -> character.classes.rogue.abilities.SliceAndDice()
            character.classes.rogue.abilities.BladeFlurry.name -> character.classes.rogue.abilities.BladeFlurry()
            character.classes.rogue.abilities.Hemorrhage.name -> character.classes.rogue.abilities.Hemorrhage()
            character.classes.rogue.abilities.Preparation.name -> character.classes.rogue.abilities.Preparation()
            character.classes.rogue.abilities.GhostlyStrike.name -> character.classes.rogue.abilities.GhostlyStrike()
            character.classes.rogue.abilities.ColdBlood.name -> character.classes.rogue.abilities.ColdBlood()
            Envenom.name -> Envenom()
            Shiv.name -> Shiv()
            Ambush.name -> Ambush()
            Eviscerate.name -> Eviscerate()
            SinisterStrike.name -> SinisterStrike()
            Backstab.name -> Backstab()
            ExposeArmor.name -> ExposeArmor()
            DeadlyPoisonMainhand.name -> DeadlyPoisonMainhand()
            InstantPoisonMainhand.name -> InstantPoisonMainhand()
            WoundPoisonMainhand.name -> WoundPoisonMainhand()
            DeadlyPoisonOffhand.name -> DeadlyPoisonOffhand()
            InstantPoisonOffhand.name -> InstantPoisonOffhand()
            WoundPoisonOffhand.name -> WoundPoisonOffhand()
            Vanish.name -> Vanish()
            Garrote.name -> Garrote()
            Rupture.name -> Rupture()
            else -> null
        }
    }

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return when(name.trim()) {
            character.classes.rogue.talents.GhostlyStrike.name -> character.classes.rogue.talents.GhostlyStrike(ranks)
            character.classes.rogue.talents.Mutilate.name -> character.classes.rogue.talents.Mutilate(ranks)
            character.classes.rogue.talents.AdrenalineRush.name -> character.classes.rogue.talents.AdrenalineRush(ranks)
            character.classes.rogue.talents.Hemorrhage.name -> character.classes.rogue.talents.Hemorrhage(ranks)
            character.classes.rogue.talents.BladeFlurry.name -> character.classes.rogue.talents.BladeFlurry(ranks)
            character.classes.rogue.talents.Preparation.name -> character.classes.rogue.talents.Preparation(ranks)
            character.classes.rogue.talents.ColdBlood.name -> character.classes.rogue.talents.ColdBlood(ranks)
            CombatPotency.name -> CombatPotency(ranks)
            Opportunity.name -> Opportunity(ranks)
            Aggression.name -> Aggression(ranks)
            ImprovedAmbush.name -> ImprovedAmbush(ranks)
            Precision.name -> Precision(ranks)           
            ImprovedEviscerate.name -> ImprovedEviscerate(ranks)
            Premeditation.name -> Premeditation(ranks)
            BladeTwisting.name -> BladeTwisting(ranks)
            ImprovedExposeArmor.name -> ImprovedExposeArmor(ranks)           
            Camouflage.name -> Camouflage(ranks)
            ImprovedGouge.name -> ImprovedGouge(ranks)
            PuncturingWounds.name -> PuncturingWounds(ranks)  
            ImprovedKick.name -> ImprovedKick(ranks)
            QuickRecovery.name -> QuickRecovery(ranks)
            ImprovedKidneyShot.name -> ImprovedKidneyShot(ranks)
            RelentlessStrikes.name -> RelentlessStrikes(ranks)
            DaggerSpecialization.name -> DaggerSpecialization(ranks)
            ImprovedPoisons.name -> ImprovedPoisons(ranks)
            RemorselessAttacks.name -> RemorselessAttacks(ranks)
            DeadenedNerves.name -> DeadenedNerves(ranks)
            ImprovedSinisterStrike.name -> ImprovedSinisterStrike(ranks)
            Riposte.name -> Riposte(ranks)
            Deadliness.name -> Deadliness(ranks)
            ImprovedSliceAndDice.name -> ImprovedSliceAndDice(ranks)
            Ruthlessness.name -> Ruthlessness(ranks)
            Deflection.name -> Deflection(ranks)
            ImprovedSprint.name -> ImprovedSprint(ranks)
            SealFate.name -> SealFate(ranks)
            DirtyDeeds.name -> DirtyDeeds(ranks)
            Initiative.name -> Initiative(ranks)
            SerratedBlades.name -> SerratedBlades(ranks)
            DirtyTricks.name -> DirtyTricks(ranks)
            Lethality.name -> Lethality(ranks)
            Setup.name -> Setup(ranks)
            DualWieldSpecialization.name -> DualWieldSpecialization(ranks)
            LightningReflexes.name -> LightningReflexes(ranks)
            Shadowstep.name -> Shadowstep(ranks)
            Elusiveness.name -> Elusiveness(ranks)
            MaceSpecialization.name -> MaceSpecialization(ranks)
            SinisterCalling.name -> SinisterCalling(ranks)
            Endurance.name -> Endurance(ranks)
            Malice.name -> Malice(ranks)
            SleightOfHand.name -> SleightOfHand(ranks)
            EnvelopingShadows.name -> EnvelopingShadows(ranks)
            MasterOfDeception.name -> MasterOfDeception(ranks)
            SurpriseAttacks.name -> SurpriseAttacks(ranks)
            FindWeakness.name -> FindWeakness(ranks)
            MasterOfSubtlety.name -> MasterOfSubtlety(ranks)
            SwordSpecialization.name -> SwordSpecialization(ranks)
            FistWeaponSpecialization.name -> FistWeaponSpecialization(ranks)
            MasterPoisoner.name -> MasterPoisoner(ranks)
            Vigor.name -> Vigor(ranks)
            FleetFooted.name -> FleetFooted(ranks)
            Murder.name -> Murder(ranks)
            VilePoisons.name -> VilePoisons(ranks)
            Vitality.name -> Vitality(ranks)
            HeightenedSenses.name -> HeightenedSenses(ranks)
            NervesOfSteel.name -> NervesOfSteel(ranks)
            WeaponExpertise.name -> WeaponExpertise(ranks)
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
