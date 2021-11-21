package character.classes.rogue.debuffs

import character.Ability
import character.*
import character.classes.rogue.talents.*
import data.Constants
import sim.Event
import sim.SimParticipant
import character.Proc
import sim.EventResult
import sim.EventType

class RuptureDot(owner: SimParticipant, consumedComboPoints: Int) : Debuff(owner) {
    companion object {
        const val name = "Rupture (DoT)"
    }

    override val name: String = Companion.name
    override val icon: String = "ability_rogue_rupture.jpg"
    override val durationMs: Int = getDurationForCombopoints(owner, consumedComboPoints)
    override val tickDeltaMs: Int = 2000

    val dmgPerTick = getDamageForCombopoints(owner, consumedComboPoints) / (durationMs / tickDeltaMs)

    // can only replace if it has higher dmg
    override val mutex: List<Mutex> = listOf(Mutex.DEBUFF_RUPTURE_DOT)
    override fun mutexPriority(sp: SimParticipant): Map<Mutex, Int> {
        return mapOf(
            Mutex.DEBUFF_RUPTURE_DOT to dmgPerTick.toInt()
        )
    }

    fun getDurationForCombopoints(sp: SimParticipant, consumedComboPoints: Int): Int {
        val timeMs: Int = when(consumedComboPoints){
            1 -> 8000
            2 -> 10000
            3 -> 12000
            4 -> 14000
            5 -> 16000
            else -> 0
        }
        return timeMs
    }

    fun getDamageForCombopoints(sp: SimParticipant, consumedComboPoints: Int): Double {

        val sb = sp.character.klass.talents[SerratedBlades.name] as SerratedBlades?
        var increasedDamagePercent = sb?.increasedDamagePercent() ?: 0.0

        val mangle = sp.sim.target.debuffs[data.abilities.raid.Mangle.name]
        if (mangle != null) {
            increasedDamagePercent += 30.0
        }

        val dmgMultiplier = 1 + (increasedDamagePercent / 100.0).coerceAtLeast(0.0)

        val damage: Double = when(consumedComboPoints){
            1 -> 324.0 + sp.attackPower() * 0.04
            2 -> 460.0 + sp.attackPower() * 0.10
            3 -> 618.0 + sp.attackPower() * 0.18
            4 -> 798.0 + sp.attackPower() * 0.21
            5 -> 1000.0 + sp.attackPower() * 0.24
            else -> 0.0
        }
        return damage * dmgMultiplier
    }

    val dot = object : Ability() {
        override val id: Int = 26867
        override val name: String = Companion.name
        override val icon: String = "ability_rogue_rupture.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                ability = this,
                amount = dmgPerTick,
                result = EventResult.HIT,
            )
            owner.logEvent(event)

            owner.fireProc(listOf(Proc.Trigger.PHYSICAL_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        dot.cast(owner)
    }
}
