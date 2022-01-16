package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedDemoralizingShout
import sim.SimParticipant

class DemoralizingShout : Ability() {
    companion object {
        const val name = "Demoralizing Shout"
    }

    override val id: Int = 25203
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_warcry.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    fun debuff(owner: SimParticipant) = object : Debuff(owner) {
        override val name: String = Companion.name
        override val icon: String = "ability_warrior_warcry.jpg"
        override val durationMs: Int = 30000
        override val hidden: Boolean = false

        override fun modifyStats(sp: SimParticipant): Stats {
            val impDemoRanks = sp.character.klass.talentRanks(ImprovedDemoralizingShout.name) ?: 0
            return Stats(
                attackPower = (-300 * (1.0 + (0.08 * impDemoRanks))).toInt()
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.target.addDebuff(debuff(sp))
    }
}
