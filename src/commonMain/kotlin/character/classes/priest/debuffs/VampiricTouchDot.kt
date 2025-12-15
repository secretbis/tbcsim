package character.classes.priest.debuffs

import character.Ability
import character.Debuff
import character.Buff
import character.Proc
import character.Resource
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class VampiricTouchDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name: String = "Vampiric Touch (DoT)"
    }

    override val name: String = Companion.name
    override val icon: String = "spell_holy_stoicism.jpg"
    override val tickDeltaMs: Int = 3000
    override val durationMs: Int = 15000

    val school = Constants.DamageType.SHADOW
    val snapShotSpellPower = owner.spellDamageWithSchool(school)
    var baseDamage: Double = 130.0
    var spellPowerCoeff = 0.2

    val ability = object : Ability() {
        override val id: Int = 34917
        override val name: String = Companion.name
        override val icon: String = "spell_holy_stoicism.jpg"

        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val damageRoll: Double = Spell.baseDamageRollSingle(owner, baseDamage, school, spellPowerCoeff, snapShotSpellPower)

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
        ability.cast(sp)
    }
}
