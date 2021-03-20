package character.classes.warlock

import character.*
import character.classes.warlock.abilities.*
import character.classes.warlock.abilities.Conflagrate
import character.classes.warlock.talents.*
import character.classes.warlock.talents.AmplifyCurse as AmplifyCurseTalent
import character.classes.warlock.talents.Conflagrate as ConflagrateTalent
import character.classes.warlock.talents.DemonicSacrifice as DemonicSacrificeTalent
import character.classes.warlock.talents.Shadowburn as ShadowburnTalent
import character.classes.warlock.talents.SiphonLife as SiphonLifeTalent
import character.classes.warlock.talents.UnstableAffliction as UnstableAfflictionTalent
import data.model.Item

class Warlock(talents: Map<String, Talent>) : Class(talents) {
    override val baseStats: Stats = Stats(
        agility = 170,
        intellect = 180,
        strength = 96,
        stamina = 154,
        spirit = 199
    )
    override val buffs: List<Buff> = listOf()

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            AmplifyCurse.name -> AmplifyCurse()
            Conflagrate.name -> Conflagrate()
            Corruption.name -> Corruption()
            CurseOfAgony.name -> CurseOfAgony()
            CurseOfDoom.name -> CurseOfDoom()
            DemonicSacrificeImp.name -> DemonicSacrificeImp()
            DemonicSacrificeSuccubus.name -> DemonicSacrificeSuccubus()
            DemonicSacrificeFelguard.name -> DemonicSacrificeFelguard()
            FelArmor.name -> FelArmor()
            Immolate.name -> Immolate()
            Incinerate.name -> Incinerate()
            LifeTap.name -> LifeTap()
            SearingPain.name -> SearingPain()
            ShadowBolt.name -> ShadowBolt()
            Shadowburn.name -> Shadowburn()
            SiphonLife.name -> SiphonLife()
            UnstableAffliction.name -> UnstableAffliction()
            else -> null
        }
    }

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return when(name) {
            AmplifyCurseTalent.name -> AmplifyCurseTalent(ranks)
            Backlash.name -> Backlash(ranks)
            Bane.name -> Bane(ranks)
            Cataclysm.name -> Cataclysm(ranks)
            ConflagrateTalent.name -> ConflagrateTalent(ranks)
            Contagion.name -> Contagion(ranks)
            DemonicAegis.name -> DemonicAegis(ranks)
            DemonicSacrificeTalent.name -> DemonicSacrificeTalent(ranks)
            Devastation.name -> Devastation(ranks)
            Emberstorm.name -> Emberstorm(ranks)
            EmpoweredCorruption.name -> EmpoweredCorruption(ranks)
            FelIntellect.name -> FelIntellect(ranks)
            ImprovedCorruption.name -> ImprovedCorruption(ranks)
            ImprovedCurseOfAgony.name -> ImprovedCurseOfAgony(ranks)
            ImprovedHealthstone.name -> ImprovedHealthstone(ranks)
            ImprovedImmolate.name -> ImprovedImmolate(ranks)
            ImprovedLifeTap.name -> ImprovedLifeTap(ranks)
            ImprovedSearingPain.name -> ImprovedSearingPain(ranks)
            ImprovedShadowBolt.name -> ImprovedShadowBolt(ranks)
            Malediction.name -> Malediction(ranks)
            Nightfall.name -> Nightfall(ranks)
            Ruin.name -> Ruin(ranks)
            ShadowAndFlame.name -> ShadowAndFlame(ranks)
            ShadowburnTalent.name -> ShadowburnTalent(ranks)
            ShadowMastery.name -> ShadowMastery(ranks)
            SiphonLifeTalent.name -> SiphonLifeTalent(ranks)
            Suppression.name -> Suppression(ranks)
            UnstableAfflictionTalent.name -> UnstableAfflictionTalent(ranks)
            else -> null
        }
    }

    override val resourceType: Resource.Type = Resource.Type.MANA
    override val canDualWield: Boolean = false
    override val attackPowerFromAgility: Int = 0
    override val attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override val dodgePctPerAgility: Double = 1.0 / 25.0
    override val baseDodgePct: Double = 2.03
    override val rangedAttackPowerFromAgility: Int = 0
    override val baseMana: Int = 2871
    override val baseSpellCritChance: Double = 1.701
}
