package character.classes.priest.abilities

import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.Ability
import character.Proc
import character.classes.priest.debuffs.ShadowWordPainDot
import character.classes.priest.talents.*
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class ShadowWordPain : Ability() {
    companion object {
        const val name = "Shadow Word Pain"
    }

    override val id: Int = 25368
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_shadowwordpain.jpg"

    val school = Constants.DamageType.SHADOW

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseResourceCost = 575.0
    override fun resourceCost(sp: SimParticipant): Double {
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?

        if (innerFocusBuff != null) {
            return 0.0
        }

        return baseResourceCost
    }

    override fun cast(sp: SimParticipant) {
        val iswp: ImprovedShadowWordPain? = sp.character.klass.talentInstance(ImprovedShadowWordPain.name)
        val iswpTicks = iswp?.currentRank ?: 0
        val sfTalent: ShadowFocus? = sp.character.klass.talentInstance(ShadowFocus.name)
        val sfHit = sfTalent?.shadowHitIncreasePct() ?: 0.0

        val result = Spell.attackRoll(sp, 0.0, school, isBinary = true, bonusHitChance = sfHit, canCrit = false)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            ability = this,
            result = result.second,
        )
        sp.logEvent(event)

        if(result.second == EventResult.RESIST){
            sp.fireProc(listOf(Proc.Trigger.SPELL_RESIST), listOf(), this, event)
            return
        }

        sp.fireProc(listOf(Proc.Trigger.SPELL_HIT), listOf(), this, event)

        sp.sim.target.addDebuff(ShadowWordPainDot(sp))
    }
}
