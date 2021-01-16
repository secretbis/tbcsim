package character

abstract class Proc {
    // A static proc is basically just a hidden buff
    // Lots of items are implemented with stuff like AP bonuses as "spells/procs"
    open val static: Boolean = false
    open val trigger: Int = 0
    open val ppm: Double = 0.0

    abstract fun proc(character: Character)
}
