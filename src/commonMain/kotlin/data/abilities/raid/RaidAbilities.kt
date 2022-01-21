package data.abilities.raid

import character.Ability
import kotlin.js.JsExport

// These are spells that are provided by other players in the raid, and may carry some
//  assumptions about stacks and talents, and when they are applied
@JsExport
object RaidAbilities {
    val raidBuffs: Array<Ability> = arrayOf(
        BlessingOfKings(),
        BlessingOfSalvation(),
        Bloodlust(),
        DivineSpirit(),
        DrumsOfBattle(),
        FerociousInspiration(1),
        FerociousInspiration(2),
        FerociousInspiration(3),
        GraceOfAirTotem(),
        HeroicPresence(),
        ImprovedBattleShout(),
        ImprovedBlessingOfMight(),
        ImprovedBlessingOfWisdom(),
        ImprovedDivineSpirit(),
        ImprovedMarkOfTheWild(),
        ImprovedSanctityAura(),
        ImprovedSealOfTheCrusader(),
        InspiringPresence(),
        LeaderOfThePack(),
        ManaSpringTotem(),
        MoonkinAura(),
        SapphireWrathBattleShout(),
        StrengthOfEarthTotem(),
        Thorns(),
        TotemOfWrath(),
        UnleashedRage(),
        VampiricTouch(1000),
        VampiricTouch(1500),
        WindfuryTotem(),
        WindfuryTotemRank1(),
        WrathOfAirTotem()
    )

    val raidDebuffs: Array<Ability> = arrayOf(
        BloodFrenzy(),
        CurseOfRecklessness(),
        CurseOfTheElements(),
        DemoralizingShout(),
        ExposeWeakness(750),
        ExposeWeakness(1000),
        ExposeWeakness(1250),
        FaerieFire(),
        ImprovedExposeArmor(),
        ImprovedFaerieFire(),
        ImprovedHuntersMark(),
        ImprovedScorch(),
        JudgementOfWisdom(),
        Malediction(),
        Misery(),
        ShadowWeaving(),
        Stormstrike(),
        SunderArmor(),
        ThunderClap(),
        Mangle()
    )

    val buffNames: Array<String> = raidBuffs.map { it.name }.toTypedArray()
    val debuffNames: Array<String> = raidDebuffs.map { it.name }.toTypedArray()

    val byName: Map<String, Ability> = (raidBuffs + raidDebuffs).map { it.name to it }.toMap()
}
