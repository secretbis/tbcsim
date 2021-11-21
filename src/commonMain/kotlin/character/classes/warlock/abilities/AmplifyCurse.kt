package character.classes.warlock.abilities

import character.Ability
import character.Buff
import character.Resource
import character.classes.warlock.talents.AmplifyCurse
import sim.SimParticipant

class AmplifyCurse : Ability() {
    companion object {
        const val name = "Amplify Curse"
    }
    override val id: Int = 18288
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_contagion.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun available(sp: SimParticipant): Boolean {
        return (sp.character.klass.talents[AmplifyCurse.name]?.currentRank ?: 0) > 0
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.MANA
    override fun resourceCost(sp: SimParticipant): Double = 0.0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_shadow_contagion.jpg"
        override val durationMs: Int = 30000
        override val hidden: Boolean = true
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
