package character.classes.priest.pet.buffs

import character.Buff
import character.Stats
import sim.SimParticipant

class ShadowfiendBase: Buff() {
    override val name: String = "Shadowfiend Base"
    override val icon: String = "spell_shadow_shadowfiend.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sp: SimParticipant): Stats {
        if (sp.owner == null) return Stats()

        // https://web.archive.org/web/20071201221602/http://www.shadowpriest.com/viewtopic.php?t=7616
        // Derived from http://elitistjerks.com/506359-post39.html
        return Stats(
            armor = (0.35 * sp.owner.armor()).toInt(),
            stamina = (0.3 * sp.owner.stamina()).toInt(),
            intellect = (0.3 * sp.owner.intellect()).toInt(),
            attackPower = (0.57 * sp.owner.spellDamage()).toInt(),
            physicalHitRating = sp.owner.stats.spellHitRating,
            meleeCritRating = sp.owner.stats.spellCritRating
        )
    }
}
