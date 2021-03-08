package character.classes.warlock.abilities

import character.Ability
import character.classes.warlock.debuffs.SiphonLifeDot
import character.classes.warlock.talents.SiphonLife
import character.classes.warlock.talents.Suppression
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimIteration

class SiphonLife : Ability() {
    companion object {
        const val name = "Siphon Life"
    }

    override val id: Int = 30911
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun resourceCost(sim: SimIteration): Double = 410.0

    override fun available(sim: SimIteration): Boolean {
        return sim.subject.klass.talents[SiphonLife.name]?.currentRank ?: 0 > 0
    }

    val dot = SiphonLifeDot()

    override fun cast(sim: SimIteration) {
        val suppression = sim.subject.klass.talents[Suppression.name] as Suppression?
        val suppressionBonusHit = suppression?.bonusAfflictionHitPct() ?: 0.0

        val school = Constants.DamageType.SHADOW
        val result = Spell.attackRoll(sim, 0.0, school, true, 0.0, suppressionBonusHit)

        val event = Event(
            eventType = Event.Type.SPELL_CAST,
            damageType = school,
            abilityName = name,
            result = result.second,
        )
        sim.logEvent(event)

        if(result.second != Event.Result.MISS) {
            sim.addDebuff(dot)
        }
    }
}
