package character.classes.priest.debuffs

import character.classes.priest.talents.*
import character.Ability
import character.Debuff
import character.Proc
import character.Resource
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class VampiricTouchDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Vampiric Touch (DoT)"
        const val manaRestoreName = "Vampiric Touch"
    }
    override val name: String = Companion.name
    override val durationMs: Int = 15000
    override val tickDeltaMs: Int = 3000
    val manaRestoreName: String = Companion.manaRestoreName

    val vtdAbility = object : Ability() {
        override val id: Int = 34917
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 103.0
        val numTicks = 5.0
        val school = Constants.DamageType.SHADOW
        override fun cast(sp: SimParticipant) {
            val spellPowerCoeff = Spell.spellPowerCoeff(0, durationMs) / numTicks
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff)

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = EventResult.HIT
            )
            owner.logEvent(event)

            // Return VT mana
            val vtdDebuff = owner.sim.target.debuffs.get(VampiricTouchDot.name)
            if(vtdDebuff != null){
                owner.addResource((damageRoll * 0.05).toInt(), Resource.Type.MANA, VampiricTouchDot.manaRestoreName)
            }

            owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        vtdAbility.cast(sp)
    }
}
