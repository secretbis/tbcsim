package character.classes.priest.abilities

import character.*
import character.classes.priest.abilities.MindFlay as MindFlayAbility

class MindFlay2 : MindFlayAbility() {
    companion object {
        const val name: String = "Mind Flay (2 ticks)"
    }

    override val name: String = Companion.name
    override val tickCount = 2
}
