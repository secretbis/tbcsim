package data.model.default

import character.Proc
import character.Ability

class UnknownProc : Proc(
    Ability(name = "Unknown", spell = EmptySpell()) {
}
