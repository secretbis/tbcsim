package character.races.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import character.classes.priest.Priest
import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.classes.priest.talents.ShadowFocus
import character.races.Undead
import character.races.debuffs.DevouringPlagueDot
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class DevouringPlague : Ability() {
    override val id: Int = 19280
    override val name: String = "Devouring Plague"

    val school = Constants.DamageType.SHADOW
    val baseDamage = 1216.0
    val baseDotTickCount = 8
    // https://wowwiki-archive.fandom.com/wiki/Spell_power_coefficient?oldid=1492745
    val spellPowerCoeff = 0.8

    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.race is Undead && sp.character.klass is Priest && super.available(sp)
    }

    val baseResourceCost = 1145.0
    override fun resourceCost(sp: SimParticipant): Double {
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?

        if (innerFocusBuff != null) {
            return 0.0
        }

        return baseResourceCost
    }

    override fun cast(sp: SimParticipant) {
        val sfTalent: ShadowFocus? = sp.character.klass.talentInstance(ShadowFocus.name)
        val sfHit = sfTalent?.shadowHitIncreasePct() ?: 0.0

        // snapshot damage on initial cast
        val damageRoll = Spell.baseDamageRollSingle(sp, baseDamage, school, spellPowerCoeff)
        val result = Spell.attackRoll(sp, damageRoll, school, isBinary = true, bonusHitChance = sfHit, canCrit = false)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            result = result.second,
        )
        sp.logEvent(event)

        if(result.first == 0.0){
            sp.fireProc(listOf(Proc.Trigger.SPELL_RESIST), listOf(), this, event)
            return
        }

        sp.fireProc(listOf(Proc.Trigger.SPELL_HIT), listOf(), this, event)

        sp.sim.target.addDebuff(DevouringPlagueDot(sp, result.first, baseDotTickCount))
    }    
}

