package data.abilities.raid

import character.Ability
import kotlin.js.JsExport

// These are spells that are provided by other players in the raid, and may carry some
//  assumptions about stacks and talents, and when they are applied
@JsExport
object RaidAbilities {
    val raidBuffs: Array<Ability> = arrayOf(
        BlessingOfKings(),
        DrumsOfBattle(),
        FerociousInspiration(1),
        FerociousInspiration(2),
        FerociousInspiration(3),
        GraceOfAirTotem(),
        ImprovedBattleShout(),
        ImprovedBlessingOfMight(),
        ImprovedBlessingOfWisdom(),
        ImprovedMarkOfTheWild(),
        ImprovedSanctityAura(),
        ImprovedSealOfTheCrusader(),
        LeaderOfThePack(),
        ManaSpringTotem(),
        MoonkinAura(),
        StrengthOfEarthTotem(),
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
        ExposeWeakness(750),
        ExposeWeakness(1000),
        ExposeWeakness(1250),
        FaerieFire(),
        ImprovedExposeArmor(),
        ImprovedScorch(),
        JudgementOfWisdom(),
        Misery(),
        ShadowWeaving(),
        Stormstrike(),
        SunderArmor(),
    )

    val buffNames: Array<String> = raidBuffs.map { it.name }.toTypedArray()
    val debuffNames: Array<String> = raidDebuffs.map { it.name }.toTypedArray()

    val byName: Map<String, Ability> = (raidBuffs + raidDebuffs).map { it.name to it }.toMap()
}
