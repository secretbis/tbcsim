package data.abilities.raid

import character.Ability
import character.classes.shaman.abilities.WrathOfAirTotem
import kotlin.js.JsExport

// These are spells that are provided by other players in the raid, and may carry some
//  assumptions about stacks and talents, and when they are applied
@JsExport
object RaidAbilities {
    val raidBuffs: Array<Ability> = arrayOf(
        BlessingOfKings(),
        FerociousInspiration(),
        GraceOfAirTotem(),
        ImprovedBattleShout(),
        ImprovedBlessingOfMight(),
        ImprovedBlessingOfWisdom(),
        ImprovedMarkOfTheWild(),
        ImprovedSanctityAura(),
        ImprovedSealOfTheCrusader(),
        LeaderOfThePack(),
        ManaSpringTotem(),
        StrengthOfEarthTotem(),
        TotemOfWrath(),
        UnleashedRage(),
        WindfuryTotem(),
        WrathOfAirTotem()
    )

    val raidDebuffs: Array<Ability> = arrayOf(
        BloodFrenzy(),
        CurseOfRecklessness(),
        CurseOfTheElements(),
        FaerieFire(),
        ImprovedExposeArmor(),
        ImprovedScorch(),
        JudgementOfWisdom(),
        Misery(),
        MoonkinAura(),
        SunderArmor(),
    )

    val buffNames: Array<String> = raidBuffs.map { it.name }.toTypedArray()
    val debuffNames: Array<String> = raidDebuffs.map { it.name }.toTypedArray()

    val byName: Map<String, Ability> = (raidBuffs + raidDebuffs).map { it.name to it }.toMap()
}
