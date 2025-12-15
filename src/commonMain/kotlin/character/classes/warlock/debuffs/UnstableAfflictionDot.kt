package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import character.Proc
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class UnstableAfflictionDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Unstable Affliction (DoT)"
    }
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_unstableaffliction_3.jpg"
    override val durationMs: Int = 18000
    override val tickDeltaMs: Int = 3000

    val dmgPerTick = 175.0
    val school = Constants.DamageType.SHADOW
    val snapshotSpellPower = owner.stats.getSpellDamage(school)
    val spellPowerCoeff = 0.2

    val ua = object : Ability() {
        override val id: Int = 30405
        override val name: String = Companion.name
        override val icon: String = "spell_shadow_unstableaffliction_3.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff, snapshotSpellPower)

            // Each tick can still resist partially
            val result = Spell.partialResistRoll(
                owner,
                Pair(damageRoll, EventResult.HIT),
                school
            )

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                ability = this,
                amount = result.first,
                result = result.second
            )
            owner.logEvent(event)

            owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        ua.cast(owner)
    }
}
