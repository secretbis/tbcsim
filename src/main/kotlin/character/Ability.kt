package character

import data.model.ItemProc

abstract class Ability {
    abstract var spell: ItemProc
    abstract fun available(): Boolean
    abstract fun cast()
}
