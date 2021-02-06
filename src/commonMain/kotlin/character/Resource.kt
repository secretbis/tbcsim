package character

import sim.SimIteration

class Resource(
    val sim: SimIteration
) {
    enum class Type {
        MANA,
        RAGE,
        ENERGY
    }

    val type: Type = sim.subject.klass.resourceType

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
        return sim.subject.klass.baseMana
            // Add intellect
            .let {
                it + when {
                    sim.intellect() <= 20 -> sim.intellect()
                    else -> 20 + (15 * (sim.intellect() - 20))
                }
            }
            .let {
                it + sim.subjectStats.manaFlatModifier
            }
            .let {
                (it * sim.subjectStats.manaMultiplier).toInt()
            }

    }

    private fun max(): Int {
        return when(type) {
            Type.MANA -> maxMana()
            Type.RAGE -> 100
            Type.ENERGY -> 100
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
