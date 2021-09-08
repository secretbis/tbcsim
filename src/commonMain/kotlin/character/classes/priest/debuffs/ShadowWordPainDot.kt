package character.classes.priest.debuffs

import character.Ability
import character.Debuff
import character.Buff
import character.Proc
import character.Resource
import character.classes.priest.talents.*
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class ShadowWordPainDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name: String = "Shadow Word Pain (DoT)"
    }

    override val name: String = Companion.name
    override val tickDeltaMs: Int = 3000
    override val durationMs: Int get() {
        val iswp: ImprovedShadowWordPain? = owner.character.klass.talentInstance(ImprovedShadowWordPain.name)
        val iswpTicks = iswp?.currentRank ?: 0

        // 6 base ticks + 1 tick per talent point
        return tickDeltaMs * (6 + iswpTicks)
    }
    
    val school = Constants.DamageType.SHADOW
    val snapShotSpellPower = owner.spellDamageWithSchool(school).toDouble()
    var baseDotDamage: Double = 206.0
    val baseDotSpellCoeff = 0.1833

    val ability = object : Ability() {
        override val id: Int = 25368
        override val name: String = Companion.name

        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val damageRoll: Double = Spell.baseDamageRollFromSnapShot(baseDotDamage, snapShotSpellPower, baseDotSpellCoeff)
            val result = Spell.attackRoll(owner, damageRoll, school, canCrit = false, canResist = false)

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                abilityName = name,
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
