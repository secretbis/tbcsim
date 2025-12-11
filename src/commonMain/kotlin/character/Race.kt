package character

import character.races.*
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
abstract class Race {
    companion object {
        fun fromString(name: String): Race? {
            return when(name.lowercase().trim()) {
                "blood elf" -> return BloodElf()
                "draenei" -> return Draenei()
                "dwarf" -> return Dwarf()
                "gnome" -> return Gnome()
                "human" -> return Human()
                "night elf" -> return NightElf()
                "orc" -> return Orc()
                "tauren" -> return Tauren()
                "troll" -> return Troll()
                "undead" -> return Undead()
                else -> null
            }
        }
    }

    abstract var baseStats: Stats
    abstract fun racialByName(name: String): Ability?
    abstract fun buffs(sp: SimParticipant): List<Buff>
}
