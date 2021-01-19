package character

import sim.Sim

abstract class Ability(val sim: Sim) {
    abstract val name: String
    abstract fun available(): Boolean
    abstract fun cast(free: Boolean = false)
    abstract fun castTimeMs(): Double
    abstract fun gcdMs(): Double
}
