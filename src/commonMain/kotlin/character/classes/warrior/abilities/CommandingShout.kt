package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.CommandingPresence
import sim.Event
import sim.EventType
import sim.SimParticipant

class CommandingShout : Ability() {
    companion object {
        const val name: String = "Commanding Shout"
    }

    override val id: Int = 469
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_rallyingcry.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double = 10.0

    private fun totalHp(sp: SimParticipant): Int {
        val cmdPres = sp.character.klass.talents[CommandingPresence.name] as CommandingPresence?
        return (1080.0 * (cmdPres?.shoutMultiplier() ?: 1.0)).toInt()
    }

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "ability_warrior_rallyingcry.jpg"
        override val durationMs: Int = 120000

        override val mutex: List<Mutex> = listOf(Mutex.BUFF_COMMANDING_SHOUT)
        override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
            return mapOf(
                Mutex.BUFF_COMMANDING_SHOUT to totalHp(sp)
            )
        }

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                healthFlatModifier = totalHp(sp)
            )
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
        sp.logEvent(
            Event(
                eventType = EventType.THREAT,
                amount = 68.0 * 5,
                ability = this
            )
        )
    }
}
