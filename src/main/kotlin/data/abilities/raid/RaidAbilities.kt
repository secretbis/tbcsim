package data.abilities.raid

import character.Ability

// These are spells that are provided by other players in the raid, and may carry some
//  assumptions about stacks and talents, and when they are applied
object RaidAbilities {
    fun byName(name: String): Ability? {
        return when(name) {
            BlessingOfKings.name -> BlessingOfKings()
            BlessingOfWisdom.name -> BlessingOfWisdom()
            BloodFrenzy.name -> BloodFrenzy()
            CurseOfRecklessness.name -> CurseOfRecklessness()
            CurseOfShadow.name -> CurseOfShadow()
            CurseOfTheElements.name -> CurseOfTheElements()
            FaerieFire.name -> FaerieFire()
            FerociousInspiration.name -> FerociousInspiration()
            ImprovedBattleShout.name -> ImprovedBattleShout()
            ImprovedBlessingOfMight.name -> ImprovedBlessingOfMight()
            ImprovedExposeArmor.name -> ImprovedExposeArmor()
            ImprovedMarkOfTheWild.name -> ImprovedMarkOfTheWild()
            ImprovedSanctityAura.name -> ImprovedSanctityAura()
            ImprovedScorch.name -> ImprovedScorch()
            ImprovedSealOfTheCrusader.name -> ImprovedSealOfTheCrusader()
            JudgementOfWisdom.name -> JudgementOfWisdom()
            LeaderOfThePack.name -> LeaderOfThePack()
            Misery.name -> Misery()
            SunderArmor.name -> SunderArmor()
            else -> null
        }
    }
}
