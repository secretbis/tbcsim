package data.abilities.generic

import character.*
import sim.SimParticipant

class ScrollOfSpiritV : Ability() {
    companion object {
        const val name = "Scroll of Spirit V"
    }

    override val id: Int = 27501
    override val name: String = Companion.name
    override val icon: String = "inv_scroll_01.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Scroll of Spirit V"
        override val icon: String = "inv_scroll_01.jpg"
        override val durationMs: Int = 30 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.BUFF_SPIRIT)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spirit = 30)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
