package data.abilities.raid

import character.Ability
import character.Buff
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

open class WindfuryTotem(val baseApBonus: Double, val abilityId: Int, val abilityName: String) : Ability() {
    constructor() : this(445.0, 25587, "Windfury Totem")

    override val id: Int = abilityId
    override val name: String = abilityName
    override fun gcdMs(sim: SimIteration): Int = 0

    val weaponBuff = object : Buff() {
        override val name: String = "Windfury Totem (Weapon)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val wfTotemAbility = object : Ability() {
            // Assume Enhancing Totems
            val totalExtraAp = baseApBonus * 1.3
            override val id: Int = abilityId
            override val name: String = abilityName

            override fun gcdMs(sim: SimIteration): Int = 0

            override fun cast(sim: SimIteration) {
                // Do attack
                val mh = sim.subject.gear.mainHand
                val attack = Melee.baseDamageRoll(sim, mh, totalExtraAp.toInt())
                val result = Melee.attackRoll(sim, attack, mh, isWhiteDmg = true)

                val event = Event(
                    eventType = Event.Type.DAMAGE,
                    damageType = Constants.DamageType.PHYSICAL,
                    isWhiteDamage = true,
                    abilityName = name,
                    amount = result.first,
                    result = result.second,
                )
                sim.logEvent(event)

                // Proc anything that can proc off a white hit
                // TODO: Should I fire procs off miss/dodge/parry/etc?
                val triggerTypes = when(result.second) {
                    Event.Result.HIT -> listOf(Proc.Trigger.MELEE_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                    Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
                    else -> null
                }

                if(triggerTypes != null) {
                    sim.fireProc(triggerTypes, listOf(mh), this, event)
                }
            }
        }

        val weaponProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                // TODO: Per random internet forums, this only procs off autos
                //       Needs confirmation
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
            )

            override val type: Type = Type.PERCENT
            override fun percentChance(sim: SimIteration): Double = 20.0

            override fun shouldProc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
                val isMhWeapon = items?.first() === sim.subject.gear.mainHand
                val mhHasNoTempEnh = sim.subject.gear.mainHand.temporaryEnhancement == null
                return isMhWeapon && mhHasNoTempEnh && super.shouldProc(sim, items, ability, event)
            }

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                wfTotemAbility.cast(sim)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(weaponProc)
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(weaponBuff)
    }
}
