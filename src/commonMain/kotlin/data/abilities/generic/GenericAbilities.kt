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
            ElixirOfMajorDefense.name -> ElixirOfMajorDefense()
            ElixirOfMajorStrength.name -> ElixirOfMajorStrength()
            FlaskOfBlindingLight.name -> FlaskOfBlindingLight()
            FlaskOfFortification.name -> FlaskOfFortification()
            FlaskOfPureDeath.name -> FlaskOfPureDeath()
            FlaskOfRelentlessAssault.name -> FlaskOfRelentlessAssault()
            HastePotion.name -> HastePotion()
            InsaneStrengthPotion.name -> InsaneStrengthPotion()
            IronshieldPotion.name -> IronshieldPotion()
            RoastedClefthoof.name -> RoastedClefthoof()
            SpicyCrawdad.name -> SpicyCrawdad()
            SpicyHotTalbuk.name -> SpicyHotTalbuk()
            SuperManaPotion.name -> SuperManaPotion()
            UseActiveTrinket.name -> UseActiveTrinket()
            WarpBurger.name -> WarpBurger()
            else -> null
        }
    }
}
