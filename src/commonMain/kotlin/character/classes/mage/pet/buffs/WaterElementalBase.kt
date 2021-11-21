package character.classes.mage.pet.buffs

import character.Buff
import character.Stats
import sim.SimParticipant

class WaterElementalBase: Buff() {
    override val name: String = "Water Elemental Base"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val icon: String = "spell_frost_summonwaterelemental_2.jpg"

    override fun modifyStats(sp: SimParticipant): Stats {
        // Inherit stats from the caster
        return Stats(
            armor = (0.35 * sp.armor()).toInt(),
            stamina = (0.3 * sp.stamina()).toInt(),
            intellect = (0.3 * sp.intellect()).toInt(),
            spellDamage = (0.33 * sp.spellDamage()).toInt()
        )
    }
}
