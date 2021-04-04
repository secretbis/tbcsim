package character.classes.hunter

import character.*
import character.classes.hunter.abilities.*
import character.classes.hunter.talents.*
import character.classes.hunter.talents.AimedShot as AimedShotTalent
import character.classes.hunter.talents.BestialWrath as BestialWrathTalent
import data.model.Item
import mechanics.Rating
import sim.SimParticipant

class Hunter(talents: Map<String, Talent>, spec: Spec) : Class(talents, spec){
    override val baseStats: Stats = Stats(
        agility = 222,
        intellect = 147,
        strength = 153,
        stamina = 154,
        spirit = 123
    )

    // Every quiver adds 15% haste, so there's no point in selecting it
    val quiverHaste = object : Buff() {
        override val name: String = "Quiver Haste"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHasteRating = 15.0 * Rating.hastePerPct)
        }
    }

    override val buffs: List<Buff> = listOf(quiverHaste)

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            ArcaneShot.name -> ArcaneShot()
            BestialWrath.name -> BestialWrath()
            HuntersMark.name -> HuntersMark()
            KillCommand.name -> KillCommand()
            MultiShot.name -> MultiShot()
            RapidFire.name -> RapidFire()
            SteadyShot.name -> SteadyShot()
            else -> null
        }
    }

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return when(name) {
            AimedShotTalent.name -> AimedShotTalent(ranks)
            AnimalHandler.name -> AnimalHandler(ranks)
            Barrage.name -> Barrage(ranks)
            BestialDiscipline.name -> BestialDiscipline(ranks)
            BestialWrathTalent.name -> BestialWrathTalent(ranks)
            CarefulAim.name -> CarefulAim(ranks)
            CombatExperience.name -> CombatExperience(ranks)
            Efficiency.name -> Efficiency(ranks)
            ExposeWeakness.name -> ExposeWeakness(ranks)
            FerociousInspiration.name -> FerociousInspiration(ranks)
            Ferocity.name -> Ferocity(ranks)
            FocusedFire.name -> FocusedFire(ranks)
            Frenzy.name -> Frenzy(ranks)
            GoForTheThroat.name -> GoForTheThroat(ranks)
            HumanoidSlaying.name -> HumanoidSlaying(ranks)
            ImprovedArcaneShot.name -> ImprovedArcaneShot(ranks)
            ImprovedAspectOfTheHawk.name -> ImprovedAspectOfTheHawk(ranks)
            ImprovedBarrage.name -> ImprovedBarrage(ranks)
            KillerInstinct.name -> KillerInstinct(ranks)
            LethalShots.name -> LethalShots(ranks)
            LightningReflexes.name -> LightningReflexes(ranks)
            MasterMarksman.name -> MasterMarksman(ranks)
            MasterTactician.name -> MasterTactician(ranks)
            MonsterSlaying.name -> MonsterSlaying(ranks)
            MortalShots.name -> MortalShots(ranks)
            RangedWeaponSpecialization.name -> RangedWeaponSpecialization(ranks)
            RapidKilling.name -> RapidKilling(ranks)
            SerpentsSwiftness.name -> SerpentsSwiftness(ranks)
            Surefooted.name -> Surefooted(ranks)
            SurvivalInstincts.name -> SurvivalInstincts(ranks)
            TheBeastWithin.name -> TheBeastWithin(ranks)
            ThrillOfTheHunt.name -> ThrillOfTheHunt(ranks)
            TrueshotAura.name -> TrueshotAura(ranks)
            UnleashedFury.name -> UnleashedFury(ranks)
            else -> null
        }
    }

    override val resourceType: Resource.Type = Resource.Type.MANA
    override val canDualWield: Boolean = true
    override val attackPowerFromAgility: Int = 1
    override val attackPowerFromStrength: Int = 1
    override val critPctPerAgility: Double = 1.0 / 42.0
    override val rangedAttackPowerFromAgility: Int = 1
    override val baseMana: Int = 3383
    override val baseSpellCritChance: Double = 3.6
    override val dodgePctPerAgility: Double = 1.0 / 25.0
    override val baseDodgePct: Double = -5.45
}
