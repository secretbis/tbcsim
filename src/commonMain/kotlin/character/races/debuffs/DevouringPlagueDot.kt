package character.races.debuffs

import character.Ability
import character.Debuff
import character.Buff
import character.Proc
import character.Resource
import character.classes.priest.talents.ShadowWeaving
import character.classes.priest.talents.VampiricTouch
import character.classes.priest.debuffs.ShadowWeavingDebuff
import character.classes.priest.debuffs.VampiricTouchDot
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class DevouringPlagueDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name: String = "Devouring Plague (DoT)"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 24000
    override val tickDeltaMs: Int = 3000

    val dpAbility = object : Ability() {
        override val id: Int = 19280
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 113.0
        val numTicks = 8.0
        val school = Constants.DamageType.SHADOW
        override fun cast(sp: SimParticipant) {
            val spellPowerCoeff = 0.1
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff)

            val shadowWeaving = sp.sim.target.debuffs.get(ShadowWeaving.name) as ShadowWeavingDebuff?
            val swMult = shadowWeaving?.shadowDamageMultiplierPct() ?: 1.0

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll * swMult,
                result = EventResult.HIT
            )
            owner.logEvent(event)

            val vtdDebuff = owner.sim.target.debuffs.get(VampiricTouchDot.name)
            if(vtdDebuff != null){
                owner.addResource((damageRoll * 0.05).toInt(), Resource.Type.MANA, VampiricTouchDot.manaRestoreName)
            }

            owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        dpAbility.cast(sp)
    }
}
