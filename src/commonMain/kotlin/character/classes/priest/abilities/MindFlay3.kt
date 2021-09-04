package character.classes.priest.abilities

import character.*
import character.classes.priest.abilities.MindFlay as MindFlayAbility

class MindFlay3 : MindFlayAbility() {
    companion object {
        const val name: String = "Mind Flay (3 ticks)"
    }

    override val name: String = Companion.name
}
