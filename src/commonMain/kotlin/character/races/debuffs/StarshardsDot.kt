package character.races.debuffs

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

class StarshardsDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name: String = "Starshards (DoT)"
    }

    override val name: String = Companion.name
    override val icon: String = "spell_arcane_starfire.jpg"
    override val tickDeltaMs: Int = 3000
    override val durationMs: Int = 15000

    val school = Constants.DamageType.ARCANE
    val snapShotSpellPower = owner.spellDamageWithSchool(school).toDouble()
    val baseDotDamage = 157.0
    val baseDotSpellCoeff = 0.167

    val starshardsAbility = object : Ability() {
        override val id: Int = 25446
        override val name: String = Companion.name
        override val icon: String = "spell_arcane_starfire.jpg"

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

            owner.fireProc(listOf(Proc.Trigger.ARCANE_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        starshardsAbility.cast(sp)
    }
}
