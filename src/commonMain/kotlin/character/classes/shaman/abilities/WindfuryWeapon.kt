package character.classes.shaman.abilities

import character.Ability
import character.Proc
import character.classes.shaman.talents.ElementalWeapons
import data.Constants
import data.buffs.TotemOfTheAstralWinds
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class WindfuryWeapon(override val name: String, val item: Item) : Ability() {
    companion object {
        const val name = "Windfury Weapon"
    }

    override val id: Int = 25505
    override val icon: String = "spell_nature_cyclone.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    override fun available(sp: SimParticipant): Boolean {
        return if(Melee.isOffhand(sp, item)) { sp.isDualWielding() } else true
    }

    fun fireEvents(sp: SimParticipant, result: Pair<Double, EventResult>) {
        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Proc anything that can proc off a white hit
        // TODO: Should I fire procs off miss/dodge/parry/etc?
        val triggerTypes = when (result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if (triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item), this, event)
        }
    }

    override fun cast(sp: SimParticipant) {
        // Check for modifying items
        val totemOfTheAstralWinds = sp.buffs[TotemOfTheAstralWinds.name] as TotemOfTheAstralWinds?
        val totemApBonus = totemOfTheAstralWinds?.windfuryWeaponApBonus() ?: 0

        // Apply talents
        val elementalWeapons = sp.character.klass.talents[ElementalWeapons.name] as ElementalWeapons?

        // Do attacks
        val extraAp = 445 + totemApBonus
        val attackOne = Melee.baseDamageRoll(sp, item, extraAp)
        val attackTwo = Melee.baseDamageRoll(sp, item, extraAp)

        // Per EJ, WF Weapon is yellow damage
        // https://web.archive.org/web/20080811084026/http://elitistjerks.com/f47/t15809-shaman_windfury/
        val resultOne = Melee.attackRoll(sp, attackOne, item, isWhiteDmg = false)
        val resultTwo = Melee.attackRoll(sp, attackTwo, item, isWhiteDmg = false)

        // Apply the nuttiest talent ever made
        val elementalWeaponsMod = elementalWeapons?.windfuryApMultiplier() ?: 1.0
        val finalResultOne = Pair(resultOne.first * elementalWeaponsMod, resultOne.second)
        val finalResultTwo = Pair(resultTwo.first * elementalWeaponsMod, resultTwo.second)

        fireEvents(sp, finalResultOne)
        fireEvents(sp, finalResultTwo)
    }
}
