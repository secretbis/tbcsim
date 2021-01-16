package data.model.default

import character.Character
import character.Proc
import mu.KotlinLogging

class UnknownProc(
        private val itemId: Int,
        private val itemName: String,
        private val spellId: Int,
        private val spellName: String) : Proc() {
    val logger = KotlinLogging.logger {}

    override fun proc(character: Character) {
        logger.warn { "Unknown proc $spellName ($spellId) from item $itemName ($itemId)" }
    }
}
