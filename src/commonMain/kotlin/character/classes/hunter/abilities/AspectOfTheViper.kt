package character.classes.hunter.abilities

import character.*
import sim.SimParticipant

class AspectOfTheViper : Ability() {
    companion object {
        const val name = "Aspect of the Viper"
    }

    override val name: String = Companion.name
    override val icon: String = "ability_hunter_aspectoftheviper.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun resourceCost(sp: SimParticipant): Double = 40.0
    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.MANA

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "ability_hunter_aspectoftheviper.jpg"
        override val durationMs: Int = -1
        override val mutex: List<Mutex> = listOf(Mutex.BUFF_HUNTER_ASPECT)

        override fun modifyStats(sp: SimParticipant): Stats {
            // From 2.2.0 patch notes:
            // Aspect of the Viper: This ability has received a slight redesign. The amount of mana regained will increase as the Hunter’s percentage of mana remaining decreases.
            // At about 60% mana, it is equivalent to the previous version of Aspect of the Viper.
            // Below that margin, it is better (up to twice as much mana as the old version); while above that margin, it will be less effective.
            // The mana regained never drops below 10% of intellect every 5 sec. or goes above 50% of intellect every 5 sec.
            if(sp.resources[Resource.Type.MANA] == null) return Stats()

            val currentManaPct = sp.resources[Resource.Type.MANA]!!.currentAmount / sp.resources[Resource.Type.MANA]!!.maxAmount
            val intMultiplier = when {
                currentManaPct < 0.60 -> 0.55
                currentManaPct < 0.70 -> 0.45
                currentManaPct < 0.80 -> 0.35
                currentManaPct < 0.90 -> 0.25
                else -> 0.10
            }

            return Stats(manaPer5Seconds = (sp.intellect() * intMultiplier + (0.35 * sp.character.level)).toInt())
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
