package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class SiphonLifeDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Siphon Life (DoT)"
    }

    override val name: String = Companion.name
    override val icon: String = "spell_shadow_requiem.jpg"
    override val durationMs: Int = 30000
    override val tickDeltaMs: Int = 3000

    val siphon =
        object : Ability() {
            override val id: Int = 30911
            override val name: String = Companion.name
            override val icon: String = "spell_shadow_requiem.jpg"

            override fun gcdMs(sp: SimParticipant): Int = 0

            val dmgPerTick = 63.0
            val school = Constants.DamageType.SHADOW
            val spellPowerCoeff = 0.1
            val snapshotSpellPower = owner.stats.getSpellDamage(school)

            override fun cast(sp: SimParticipant) {
                val damageRoll =
                    Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff, snapshotSpellPower)

                // Each tick can still resist partially
                val result = Spell.partialResistRoll(owner, Pair(damageRoll, EventResult.HIT), school)

                val event =
                    Event(
                        eventType = EventType.DAMAGE,
                        damageType = school,
                        ability = this,
                        amount = result.first,
                        result = result.second,
                    )
                owner.logEvent(event)
            }
        }

    override fun tick(sp: SimParticipant) {
        siphon.cast(sp)
    }
}
