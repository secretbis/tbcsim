package character.classes.rogue.abilities

import character.Ability
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Melee
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

    val baseDamage = Pair(146.0, 194.0)

    override fun cast(sp: SimParticipant) {
        val vp = sp.character.klass.talents[VilePoisons.name] as VilePoisons?
        val dmgIncrease = vp?.damageIncreasePercentEnvenom() ?: 0.0

        val dmgMultiplier = 1 + (dmgIncrease / 100.0).coerceAtLeast(0.0)

        // TODO: this needs to be casted so it can be resisted.
        //       can't use Spell.attackRoll though because it uses spellcrit/hit etc.

        val damage = Melee.baseDamageRollPure(baseDamage.first, baseDamage.second) * dmgMultiplier
        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.NATURE,
            abilityName = name,
            amount = damage,
            result = Event.Result.HIT
        )
        sp.logEvent(event)
    
        sp.fireProc(listOf(Proc.Trigger.NATURE_DAMAGE), listOf(), this, event)
    }
}
