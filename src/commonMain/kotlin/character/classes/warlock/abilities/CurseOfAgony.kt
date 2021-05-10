package character.classes.warlock.abilities

import character.Ability
import character.classes.warlock.debuffs.CurseOfAgonyDot
import character.classes.warlock.talents.Suppression
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class CurseOfAgony : Ability() {
    companion object {
        const val name = "Curse of Agony"
    }

    override val id: Int = 27218
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double = 265.0

    override fun cast(sp: SimParticipant) {
        val suppression = sp.character.klass.talents[Suppression.name] as Suppression?
        val suppressionBonusHit = suppression?.bonusAfflictionHitPct() ?: 0.0

        val school = Constants.DamageType.SHADOW
        val result = Spell.attackRoll(sp, 0.0, school, true, 0.0, suppressionBonusHit)

        val event = Event(
            eventType = EventType.SPELL_CAST,
            damageType = school,
            abilityName = name,
            result = result.second,
        )
        sp.logEvent(event)

        if(result.second != EventResult.MISS) {
            sp.sim.target.addDebuff(CurseOfAgonyDot(sp))
        }
    }
}
