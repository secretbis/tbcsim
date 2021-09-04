package character.classes.priest.abilities

import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.classes.priest.talents.InnerFocus as InnerFocusTalent
import character.Ability
import sim.SimParticipant

class InnerFocus : Ability() {
    companion object {
        const val name: String = "Inner Focus"
    }
    override val id: Int = 14751
    override val name: String = Companion.name
    
    override fun gcdMs(sp: SimParticipant): Int = 0
    
    override fun castTimeMs(sp: SimParticipant): Int = 0
    
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun available(sp: SimParticipant): Boolean {
        return super.available(sp) && sp.character.klass.hasTalentRanks(InnerFocusTalent.name)
    }

    override fun cast(sp: SimParticipant) {
        // Add buff that other stuff checks
        sp.addBuff(InnerFocusBuff())
    }
}
