package character

class Resource(
    val character: Character
) {
    enum class Type {
        MANA,
        RAGE,
        ENERGY
    }

    val type: Type = character.klass.resourceType

    val initialAmount: Int
    var maxAmount: Int
    var currentAmount: Int

    init {
        currentAmount = when(type) {
            Type.RAGE -> 0
            else -> max()
        }
        initialAmount = currentAmount
        maxAmount = max()
    }

    private fun maxMana(): Int {
        return character.klass.baseResourceAmount
            // Add intellect
            .let {
                when {
                    character.stats.intellect <= 20 -> character.stats.intellect
                    else -> 20 + (15 * (character.stats.intellect - 20))
                }
            }
            // TODO: Talents and buffs

    }

    private fun max(): Int {
        return when(type) {
            Type.MANA -> maxMana()
            Type.RAGE -> 0
            Type.ENERGY -> 100
        }
    }

    fun add(amt: Int) {
        currentAmount += amt
        if(currentAmount > maxAmount) currentAmount = maxAmount
    }

    fun subtract(amt: Int) {
        currentAmount -= amt
        assert(currentAmount >= 0) { "Resource negative - something tried to cast when it shouldn't" }
    }
}
