package character.races.debuffs

import character.Ability
import character.Debuff
import character.Buff
import character.Proc
import character.Resource
import character.classes.priest.talents.VampiricTouch
import character.classes.priest.debuffs.VampiricTouchDot
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class DevouringPlagueDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name: String = "Devouring Plague (DoT)"
    }

    override val name: String = Companion.name
    override val icon: String = "spell_shadow_blackplague.jpg"
    override val tickDeltaMs: Int = 3000
    override val durationMs: Int = 24000

    val school = Constants.DamageType.SHADOW
    val snapShotSpellPower = owner.spellDamageWithSchool(school).toDouble()
    var baseDotDamage: Double = 152.0
    val baseDotSpellCoeff = 0.1

    val dpAbility = object : Ability() {
        override val id: Int = 25467
        override val name: String = Companion.name
        override val icon: String = "spell_shadow_blackplague.jpg"

        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val damageRoll: Double = Spell.baseDamageRollFromSnapShot(baseDotDamage, snapShotSpellPower, baseDotSpellCoeff)
            val result = Spell.attackRoll(owner, damageRoll, school, canCrit = false, canResist = false)

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
        dpAbility.cast(sp)
    }
}
