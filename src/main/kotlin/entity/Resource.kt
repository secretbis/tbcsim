package entity

class Resource(val entity: Entity) {
    enum class Type {
        MANA,
        RAGE,
        ENERGY
    }

    val initialAmount: Int
    var currentAmount: Int
    val maxAmount: Int

    init {
        currentAmount = when(entity.resourceType) {
            Type.RAGE -> 0
            else -> entity.resource.max()
        }
        initialAmount = currentAmount
        maxAmount = max()
    }

    private fun maxMana(): Int {
        return entity.baseResourceAmount
            // Add intellect
            .let {
                when {
                    entity.stats.intellect <= 20 -> entity.stats.intellect
                    else -> 20 + (15 * (entity.stats.intellect - 20))
                }
            }
            // TODO: Talents

    }

    private fun max(): Int {
        return when(entity.resourceType) {
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