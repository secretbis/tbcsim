package character.classes.warlock.abilities

import character.Ability
import character.Resource
import character.classes.warlock.talents.ImprovedLifeTap
import data.Constants
import mechanics.Spell
import sim.SimParticipant

class LifeTap : Ability() {
    companion object {
        const val name = "Life Tap"
    }
    override val id: Int = 27222
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    // TODO: Life as a resource
    val manaPerCast = 580.0
    override fun cast(sp: SimParticipant) {
        val impLt = sp.character.klass.talents[ImprovedLifeTap.name] as ImprovedLifeTap?
        val impLtMultiplier = impLt?.lifeTapManaMultiplier() ?: 1.0

        // Per lock discord, coefficient is 0.8
        val spellPowerCoeff = 0.8
        val withSpellPower = Spell.baseDamageRollSingle(sp, manaPerCast, spellPowerCoeff, Constants.DamageType.SHADOW)

        val totalAmount = withSpellPower * impLtMultiplier
        sp.addResource(totalAmount.toInt(), Resource.Type.MANA, name)
    }
}
