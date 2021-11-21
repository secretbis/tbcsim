package character.classes.hunter.talents

import character.Buff
import character.CharacterType
import character.Stats
import character.Talent
import sim.SimParticipant

class MonsterSlaying(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Monster Slaying"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val icon: String = "inv_misc_head_dragon_black.jpg"

        val validSubtypes = setOf(CharacterType.BEAST, CharacterType.GIANT, CharacterType.DRAGONKIN)

        override fun modifyStats(sp: SimParticipant): Stats? {
            return if(sp.sim.target.character.subTypes.intersect(validSubtypes).isNotEmpty()) {
                Stats(
                    physicalDamageMultiplier = 1.0 + (0.01 * currentRank),
                    whiteDamageAddlCritMultiplier = 1.0 + (0.01 * currentRank),
                    yellowDamageAddlCritMultiplier = 1.0 + (0.01 * currentRank),
                )
            } else null
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
