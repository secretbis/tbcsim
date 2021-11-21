package character.classes.hunter.talents

import character.Buff
import character.CharacterType
import character.Stats
import character.Talent
import sim.SimParticipant

class HumanoidSlaying(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Humanoid Slaying"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val icon: String = "spell_holy_prayerofhealing.jpg"

        override fun modifyStats(sp: SimParticipant): Stats? {
            return if(sp.sim.target.character.subTypes.contains(CharacterType.HUMANOID)) {
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
