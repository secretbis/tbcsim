package character.races.abilities

import character.Ability
import character.Buff
import character.Resource
import character.Stats
import character.races.Troll
import mechanics.Rating
import sim.SimParticipant

class Berserking : Ability() {
    override val id: Int = 26297
    override val name: String = "Berserking"

    override fun cooldownMs(sp: SimParticipant): Int = 180000
    // https://tbc.wowhead.com/spell=20554/berserking
    override fun gcdMs(sp: SimParticipant): Int = 0

    override fun resourceType(sp: SimParticipant): Resource.Type {
        if(sp.resources.containsKey(Resource.Type.MANA)) return Resource.Type.MANA
        if(sp.resources.containsKey(Resource.Type.ENERGY)) return Resource.Type.ENERGY
        if(sp.resources.containsKey(Resource.Type.RAGE)) return Resource.Type.RAGE

        return Resource.Type.MANA
    }

    override fun resourceCost(sp: SimParticipant): Double {
        return when(resourceType(sp)) {
            Resource.Type.MANA -> 0.06 * sp.character.klass.baseMana
            Resource.Type.ENERGY -> 10.0
            Resource.Type.RAGE -> 5.0
            else -> 0.0
        }
    }

    val buff = object : Buff() {
        override val name: String = "Berserking"
        override val durationMs: Int = 10000

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                physicalHasteMultiplier = 1.1,
                spellHasteRating = 10 * Rating.hastePerPct
            )
        }
    }

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.race is Troll && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
