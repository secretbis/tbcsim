package character.classes.warlock.debuffs

import character.Ability
import character.Buff
import character.Debuff
import character.Proc
import character.classes.warlock.abilities.AmplifyCurse
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimParticipant

class CurseOfDoom(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Curse of Doom"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 60000
    override val tickDeltaMs: Int = 60000

    val doom = object : Ability() {
        override val id: Int = 27216
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 4200.0
        val school = Constants.DamageType.SHADOW
        override fun cast(sp: SimParticipant) {
            // Amplify Curse
            val ampCurseMultiplier = if(owner.buffs[AmplifyCurse.name] != null) { 1.5 } else 1.0
            owner.consumeBuff(object : Buff() {
                override val name: String = AmplifyCurse.name
                override val durationMs: Int = -1
            })

            // Per DBs this is 200% spell damage
            val spellPowerCoeff = 2.0
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, spellPowerCoeff, school) * ampCurseMultiplier

            val event = Event(
                eventType = Event.Type.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = Event.Result.HIT,
            )
            owner.logEvent(event)

            owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        doom.cast(sp)
    }
}
