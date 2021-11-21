package character.classes.mage.abilities

import character.classes.mage.buffs.PresenceOfMind as PresenceOfMindBuff
import character.Ability
import sim.SimParticipant

class PresenceOfMind : Ability() {
    companion object {
        const val name: String = "Presence of Mind"
    }
    override val id: Int = 12043
    override val name: String = Companion.name
    override val icon: String = "spell_nature_enchantarmor.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun castTimeMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun cast(sp: SimParticipant) {
        // Add buff that other stuff checks
        sp.addBuff(PresenceOfMindBuff())
    }
}
