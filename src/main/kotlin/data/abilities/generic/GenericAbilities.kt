package data.abilities.generic

import character.Ability

// These are abilities available to all players, regardless of class/race (e.g. consumables)
object GenericAbilities {
    fun byName(name: String): Ability? {
        return when(name) {
            BlackenedBasilisk.name -> BlackenedBasilisk()
            CrunchySerpent.name -> CrunchySerpent()
            DarkRune.name -> DarkRune()
            DemonicRune.name -> DemonicRune()
            DestructionPotion.name -> DestructionPotion()
            ElixirOfMajorAgility.name -> ElixirOfMajorAgility()
            FlaskOfBlindingLight.name -> FlaskOfBlindingLight()
            FlaskOfRelentlessAssault.name -> FlaskOfRelentlessAssault()
            HastePotion.name -> HastePotion()
            InsaneStrengthPotion.name -> InsaneStrengthPotion()
            DestructionPotion.name -> DestructionPotion()
            RoastedClefthoof.name -> RoastedClefthoof()
            SpicyHotTalbuk.name -> SpicyHotTalbuk()
            SuperiorWizardOil.name -> SuperiorWizardOil()
            SuperManaPotion.name -> SuperManaPotion()
            else -> null
        }
    }
}
