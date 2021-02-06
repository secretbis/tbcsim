package character

class Character(
    val klass: Class,
    val race: Race,
    val level: Int = 70,
    var gear: Gear = Gear()
)
