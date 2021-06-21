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
            armor = 5474 + (0.35 * sp.armor()).toInt(),
            stamina = (280.0 + 0.3 * sp.stamina()).toInt(),
            intellect = (133 + 0.3 * sp.intellect()).toInt(),
            strength = 153,
            agility = 108,
            spirit = 122,
            attackPower = (286.0 + 0.57 * ownerSpellDamage.toInt()).toInt(),
            physicalHitRating = (sp.owner?.stats?.spellHitRating ?: 0.0),
            meleeCritRating = (sp.owner?.stats?.spellCritRating ?: 0.0)
        )
    }
}
