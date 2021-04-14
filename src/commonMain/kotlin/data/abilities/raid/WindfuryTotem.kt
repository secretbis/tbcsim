package data.abilities.raid

import character.Ability
import character.Buff
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimParticipant

open class WindfuryTotem(val baseApBonus: Double, val abilityId: Int, val abilityName: String) : Ability() {
    constructor() : this(445.0, 25587, "Windfury Totem")

    override val id: Int = abilityId
    override val name: String = abilityName
    override fun gcdMs(sp: SimParticipant): Int = 0

    val weaponBuff = object : Buff() {
        override val name: String = "$abilityName (Weapon)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val wfTotemAbility = object : Ability() {
            // Assume Enhancing Totems
            val totalExtraAp = baseApBonus * 1.3
            override val id: Int = abilityId
            override val name: String = abilityName

            override fun gcdMs(sp: SimParticipant): Int = 0

            override fun cast(sp: SimParticipant) {
                // Do attack
                val mh = sp.character.gear.mainHand
                val attack = Melee.baseDamageRoll(sp, mh, totalExtraAp.toInt())
                val result = Melee.attackRoll(sp, attack, mh, isWhiteDmg = true)

                val event = Event(
                    eventType = Event.Type.DAMAGE,
                    damageType = Constants.DamageType.PHYSICAL,
                    isWhiteDamage = true,
                    abilityName = name,
                    amount = result.first,
                    result = result.second,
                )
                sp.logEvent(event)

                // Proc anything that can proc off a white hit
                // TODO: Should I fire procs off miss/dodge/parry/etc?
                val triggerTypes = when(result.second) {
                    Event.Result.HIT -> listOf(Proc.Trigger.MELEE_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                    Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
                    else -> null
                }

                if(triggerTypes != null) {
                    sp.fireProc(triggerTypes, listOf(mh), this, event)
                }
            }
        }

        val weaponProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_REPLACED_AUTO_ATTACK_HIT,
                Trigger.MELEE_REPLACED_AUTO_ATTACK_CRIT
            )

            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 20.0

            override fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
                val isMhWeapon = items?.first() === sp.character.gear.mainHand
                val mhHasNoTempEnh = sp.character.gear.mainHand.tempEnchant == null
                return isMhWeapon && mhHasNoTempEnh && super.shouldProc(sp, items, ability, event)
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                wfTotemAbility.cast(sp)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(weaponProc)
    }

    override fun cast(sp: SimParticipant) {
        sp.sim.addRaidBuff(weaponBuff)
    }
}
