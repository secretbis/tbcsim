package character.classes.warlock.abilities

import character.Ability
import character.classes.warlock.debuffs.UnstableAfflictionDot
import character.classes.warlock.talents.Suppression
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class UnstableAffliction : Ability() {
    companion object {
        const val name = "Unstable Affliction"
    }

    override val id: Int = 30405
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun castTimeMs(sp: SimParticipant): Int = (1500 / sp.spellHasteMultiplier()).toInt()

    override fun resourceCost(sp: SimParticipant): Double = 400.0

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.klass.talents[UnstableAffliction.name]?.currentRank ?: 0 > 0
    }

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
            sp.sim.target.addDebuff(UnstableAfflictionDot(sp))
        }
    }
}
