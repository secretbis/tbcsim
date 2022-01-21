package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Melee
import mechanics.Rating
import sim.SimParticipant

class PoleaxeSpec(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Poleaxe Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = "Poleaxe Specialization"
        override val icon: String = "inv_axe_06.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            // This doesn't account for dual-wielding different weapon types, since Fury should never take this
            return if(sp.character.gear.mainHand.isPoleaxe()) {
                val critPct = 1.0 * currentRank
                return Stats(meleeCritRating = critPct * Rating.critPerPct)
            } else Stats()
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
