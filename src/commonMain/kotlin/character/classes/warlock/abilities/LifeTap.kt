package character.classes.warlock.abilities

import character.Ability
import character.Resource
import character.classes.warlock.talents.ImprovedLifeTap
import data.Constants
import mechanics.Spell
import sim.SimIteration

class LifeTap : Ability() {
    companion object {
        const val name = "Life Tap"
    }
    override val id: Int = 27222
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    // TODO: Life as a resource
    val manaPerCast = 580.0
    override fun cast(sim: SimIteration) {
        val impLt = sim.subject.klass.talents[ImprovedLifeTap.name] as ImprovedLifeTap?
        val impLtMultiplier = impLt?.lifeTapManaMultiplier() ?: 1.0

        // Per lock discord, coefficient is 0.8
        val spellPowerCoeff = 0.8
        val withSpellPower = Spell.baseDamageRoll(sim, manaPerCast, spellPowerCoeff, Constants.DamageType.SHADOW)

        val totalAmount = withSpellPower * impLtMultiplier
        sim.addResource(totalAmount.toInt(), Resource.Type.MANA)
    }
}
