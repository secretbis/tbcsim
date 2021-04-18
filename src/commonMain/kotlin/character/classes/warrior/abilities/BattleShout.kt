package character.classes.warrior.abilities

import character.Ability
import character.Buff
import character.Resource
import character.Stats
import character.classes.warrior.talents.CommandingPresence
import sim.SimParticipant

class BattleShout : Ability() {
    companion object {
        const val name: String = "Battle Shout"
    }

    override val id: Int = 2048
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double = 10.0

    val buff = object : Buff() {
        override val name: String = "Battle Shout"
        override val durationMs: Int = 120000

        override fun modifyStats(sp: SimParticipant): Stats {
            val cmdPres = sp.character.klass.talents[CommandingPresence.name] as CommandingPresence?
            val ap = 305.0 * (cmdPres?.shoutMultiplier() ?: 1.0)

            return Stats(
                attackPower = ap.toInt()
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
