package data.abilities.generic

import character.Ability

// These are abilities available to all players, regardless of class/race (e.g. consumables)
object GenericAbilities {
    fun byName(name: String): Ability? {
        return when(name) {
            AdeptsElixir.name -> AdeptsElixir()
            BlackenedBasilisk.name -> BlackenedBasilisk()
            CrunchySerpent.name -> CrunchySerpent()
            DarkRune.name -> DarkRune()
            DemonicRune.name -> DemonicRune()
            DestructionPotion.name -> DestructionPotion()
            ElixirOfDraenicWisdom.name -> ElixirOfDraenicWisdom()
            ElixirOfMajorAgility.name -> ElixirOfMajorAgility()
            ElixirOfMajorStrength.name -> ElixirOfMajorStrength()
            FlaskOfBlindingLight.name -> FlaskOfBlindingLight()
            FlaskOfPureDeath.name -> FlaskOfPureDeath()
            FlaskOfRelentlessAssault.name -> FlaskOfRelentlessAssault()
            HastePotion.name -> HastePotion()
            Innervate.name -> Innervate()
            InsaneStrengthPotion.name -> InsaneStrengthPotion()
            RoastedClefthoof.name -> RoastedClefthoof()
            ScrollOfSpiritV.name -> ScrollOfSpiritV()
            SpicyHotTalbuk.name -> SpicyHotTalbuk()
            SuperManaPotion.name -> SuperManaPotion()
            UseActiveTrinket.name -> UseActiveTrinket()
            WarpBurger.name -> WarpBurger()
            else -> null
        }
    }
}
