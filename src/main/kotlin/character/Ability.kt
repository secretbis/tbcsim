package character

import sim.SimIteration

abstract class Ability(val sim: SimIteration) {
    abstract val id: Int
    abstract val name: String

    abstract fun available(): Boolean
    abstract fun cast(free: Boolean = false)
    abstract fun castTimeMs(): Int
    abstract fun gcdMs(): Int
}
