package character.classes.priest.pet.buffs

import character.Buff
import character.Stats
import sim.SimParticipant

class ShadowfiendBase: Buff() {
    override val name: String = "Shadowfiend Base"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sp: SimParticipant): Stats {
        // Inherit stats from the caster
        val ownerSpellDamage = sp.owner?.spellDamage() ?: 0.0

        return Stats(
            armor = (0.35 * sp.armor()).toInt(),
            stamina = (0.3 * sp.stamina()).toInt(),
            intellect = (0.3 * sp.intellect()).toInt(),
            spellDamage = (ownerSpellDamage.toInt() / 4).toInt(),
            attackPower = (ownerSpellDamage.toInt() / 4).toInt(),
            physicalHitRating = (sp.owner?.stats?.spellHitRating ?: 0.0)
        )
    }
}
