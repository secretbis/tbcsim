package character.classes.rogue.abilities

import character.Ability
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import character.classes.rogue.talents.*
import sim.EventResult
import sim.EventType

class WoundPoison(override val name: String, val item: Item) : Ability() {
    companion object {
        const val name = "Wound Poison"
    }

    override val id: Int = 27189
    override val icon: String = "inv_misc_herb_16.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd: Boolean = true

    override fun available(sp: SimParticipant): Boolean {
        return if(Melee.isOffhand(sp, item)) { sp.isDualWielding() } else true
    }

    val baseDamage = 65.0

    override fun cast(sp: SimParticipant) {

        val vp = sp.character.klass.talents[VilePoisons.name] as VilePoisons?
        val dmgIncrease = vp?.damageIncreasePercentEnvenom() ?: 0.0

        val dmgMultiplier = 1 + (dmgIncrease / 100.0).coerceAtLeast(0.0)

        // TODO: this needs to be casted so it can be resisted.
        //       can't use Spell.attackRoll though because it uses spellcrit/hit etc.
        val damage = baseDamage * dmgMultiplier

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.NATURE,
            ability = this,
            amount = damage,
            result = EventResult.HIT
        )
        sp.logEvent(event)

        sp.sim.target.addDebuff(character.classes.rogue.debuffs.WoundPoison(sp))

        sp.fireProc(listOf(Proc.Trigger.NATURE_DAMAGE), listOf(), this, event)
    }
}
