package data.abilities.generic

import character.Ability

// These are abilities available to all players, regardless of class/race (e.g. consumables)
object GenericAbilities {
    fun byName(name: String): Ability? {
        return when(name) {
            ElixirOfMajorAgility.name -> ElixirOfMajorAgility()
            FlaskOfRelentlessAssault.name -> FlaskOfRelentlessAssault()
            HastePotion.name -> HastePotion()
            InsaneStrengthPotion.name -> InsaneStrengthPotion()
            RoastedClefthoof.name -> RoastedClefthoof()
            SpicyHotTalbuk.name -> SpicyHotTalbuk()
            else -> null
        }
    }
}
