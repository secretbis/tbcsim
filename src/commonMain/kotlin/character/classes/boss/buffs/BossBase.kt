package character.classes.boss.buffs

import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class BossBase : Buff() {
    override val name: String = "Boss Base"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val icon: String = "ability_meleedamage.jpg"

    override fun modifyStats(sp: SimParticipant): Stats {
        // Fight club post about boss damage mechanics
        // https://discord.com/channels/383596811517952002/814527236241620993/861428553312370729
        val bossAp = sp.character.level * 5.5 - 81.8
        val bossCritRating = 5.6 * Rating.critPerPct

        return Stats(
            attackPower = bossAp.toInt(),
            rangedAttackPower = bossAp.toInt(),
            meleeCritRating = bossCritRating
        )
    }
}