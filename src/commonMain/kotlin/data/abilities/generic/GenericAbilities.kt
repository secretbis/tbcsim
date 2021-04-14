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
            FlaskOfPureDeath.name -> FlaskOfPureDeath()
            FlaskOfRelentlessAssault.name -> FlaskOfRelentlessAssault()
            HastePotion.name -> HastePotion()
            InsaneStrengthPotion.name -> InsaneStrengthPotion()
            RoastedClefthoof.name -> RoastedClefthoof()
            SpicyHotTalbuk.name -> SpicyHotTalbuk()
            SuperManaPotion.name -> SuperManaPotion()
            UseActiveTrinket.name -> UseActiveTrinket()
            WarpBurger.name -> WarpBurger()
            else -> null
        }
    }
}
