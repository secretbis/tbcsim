package data.abilities.raid

import character.Ability
import kotlin.js.JsExport

// These are spells that are provided by other players in the raid, and may carry some
//  assumptions about stacks and talents, and when they are applied
@JsExport
object RaidAbilities {
    val raidBuffs: Array<Ability> = arrayOf(
        BlessingOfKings(),
        ImprovedBlessingOfWisdom(),
        FerociousInspiration(),
        GraceOfAirTotem(),
        ImprovedBattleShout(),
        ImprovedBlessingOfMight(),
        ImprovedMarkOfTheWild(),
        ImprovedSanctityAura(),
        ImprovedSealOfTheCrusader(),
        LeaderOfThePack(),
        StrengthOfEarthTotem(),
        UnleashedRage(),
        WindfuryTotem(),
    )

    val raidDebuffs: Array<Ability> = arrayOf(
        BloodFrenzy(),
        CurseOfRecklessness(),
        CurseOfShadow(),
        CurseOfTheElements(),
        FaerieFire(),
        ImprovedExposeArmor(),
        ImprovedScorch(),
        JudgementOfWisdom(),
        Misery(),
        SunderArmor(),
    )

    val buffNames: Array<String> = raidBuffs.map { it.name }.toTypedArray()
    val debuffNames: Array<String> = raidDebuffs.map { it.name }.toTypedArray()

    val byName: Map<String, Ability> = (raidBuffs + raidDebuffs).map { it.name to it }.toMap()
}
