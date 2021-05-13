package character.classes.rogue.abilities

import character.Ability
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Melee
import mechanics.Spell
import sim.Event
import sim.SimParticipant
import character.classes.rogue.talents.*

class InstantPoison(override val name: String, val item: Item) : Ability() {
    companion object {
        const val name = "Instant Poison"
    }

    override val id: Int = 26890
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd: Boolean = true

    override fun available(sp: SimParticipant): Boolean {
        return if(Melee.isOffhand(sp, item)) { sp.isDualWielding() } else true
    }

    // val baseDamage = Pair(146.0, 194.0)  // VII
    val baseDamage = Pair(92.0, 118.0)      // V to test since its the one you get on beta premades

    override fun cast(sp: SimParticipant) {
        val vp = sp.character.klass.talents[VilePoisons.name] as VilePoisons?
        val dmgIncrease = vp?.damageIncreasePercentEnvenom() ?: 0.0

        val dmgMultiplier = 1 + (dmgIncrease / 100.0).coerceAtLeast(0.0)

        val damage = Melee.baseDamageRollPure(baseDamage.first, baseDamage.second) * dmgMultiplier
        val result = Spell.attackRoll(sp, damage, school = Constants.DamageType.NATURE, bonusHitChance = 100.0)
        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.NATURE,
            abilityName = name,
            amount = result.first,
            result = result.second
        )
        sp.logEvent(event)
    
        sp.fireProc(listOf(Proc.Trigger.NATURE_DAMAGE), listOf(), this, event)
    }
}
