package character.classes.mage.abilities

import character.Ability
import character.Resource
import data.itemsets.TempestRegalia
import sim.SimParticipant

class Evocation : Ability() {
    companion object {
        const val name = "Evocation"
    }

    override val id: Int = 12051
    override val name: String = Companion.name
    override val icon: String = "spell_nature_purge.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    // TODO: Create a way to describe partial evocations in rotations.  Allowing a cast time to be specified should be enough.
    val baseCastTime = 8000
    override fun castTimeMs(sp: SimParticipant): Int {
        // Check T6 two-piece bonus
        return if(sp.buffs[TempestRegalia.TWO_SET_BUFF_NAME] != null) {
            baseCastTime + 2000
        } else baseCastTime
    }
    override fun cooldownMs(sp: SimParticipant): Int = 8 * 60 * 1000

    override fun cast(sp: SimParticipant) {
        // Restores 15% of max mana per 2s, which is 60% for full channel
        val restored = (sp.resources[Resource.Type.MANA]?.maxAmount ?: 0) * (0.15 * castTimeMs(sp) / 2000)
        if(restored > 0.0) {
            sp.addResource(restored.toInt(), Resource.Type.MANA, this)
        }
    }
}
