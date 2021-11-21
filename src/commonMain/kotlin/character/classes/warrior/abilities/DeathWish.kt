package character.classes.warrior.abilities

import character.Ability
import character.Buff
import character.Resource
import character.Stats
import character.classes.warrior.talents.DeathWish as DeathWishTalent
import sim.SimParticipant

class DeathWish : Ability() {
    companion object {
        const val name = "Death Wish"
    }

    override val id: Int = 12292
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_deathpact.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double = 10.0

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.klass.talents[DeathWishTalent.name]?.currentRank == 1 && super.available(sp)
    }

    val buff = object : Buff() {
        override val name: String = "Death Wish"
        override val icon: String = "spell_shadow_deathpact.jpg"
        override val durationMs: Int = 30000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalDamageMultiplier = 1.2)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
