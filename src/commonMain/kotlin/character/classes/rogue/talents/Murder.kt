package character.classes.rogue.talents

import character.*
import sim.SimParticipant

class Murder(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Murder"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    val characterTypes = setOf(CharacterType.HUMANOID, CharacterType.GIANT, CharacterType.BEAST, CharacterType.DRAGONKIN)

    fun damageMultiplier(): Double {
        return 1.0 + (currentRank * 0.01)
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats? {
            return if(sp.sim.target.character.subTypes.intersect(characterTypes).isNotEmpty()) {
                Stats(
                    physicalDamageMultiplier = damageMultiplier(),
                )
            } else null
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}