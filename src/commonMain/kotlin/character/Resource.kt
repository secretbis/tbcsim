package character

import sim.SimParticipant

class Resource(
    val sp: SimParticipant,
    val type: Type
) {
    enum class Type {
        MANA,
        RAGE,
        ENERGY,
        FOCUS,
        COMBO_POINT
    }

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
        return sp.character.klass.baseMana
            // Add intellect
            .let {
                it + when {
                    sp.intellect() <= 20 -> sp.intellect()
                    else -> 20 + (15 * (sp.intellect() - 20))
                }
            }
            .let {
                it + sp.stats.manaFlatModifier
            }
            .let {
                (it * sp.stats.manaMultiplier).toInt()
            }

    }

    private fun max(): Int {
        return when(type) {
            Type.MANA -> maxMana()
            Type.RAGE -> 100
            Type.ENERGY -> 100
            Type.FOCUS -> 100
            Type.COMBO_POINT -> 5
        }
    }

    fun add(amt: Int) {
        currentAmount += amt
        if(currentAmount > maxAmount) currentAmount = maxAmount
    }

    fun subtract(amt: Int) {
        currentAmount -= amt
        if(currentAmount < 0) {
            throw Exception("Resource negative - something tried to cast when it shouldn't")
        }
    }
}
