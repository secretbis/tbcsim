package character

abstract class Ability {
    abstract val name: String
    abstract fun available(): Boolean
    abstract fun cast()
}
