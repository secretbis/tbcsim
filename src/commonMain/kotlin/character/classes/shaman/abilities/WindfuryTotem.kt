package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Mutex
import character.Proc
import character.classes.shaman.talents.ImprovedWeaponTotems
import character.classes.shaman.talents.MentalQuickness
import character.classes.shaman.talents.TotemicFocus
import data.Constants
import data.model.Item
import mechanics.General
import mechanics.Melee
import sim.Event
import sim.SimIteration

open class WindfuryTotem(val baseApBonus: Double, val baseManaCost: Double, val abilityId: Int, val abilityName: String): Ability() {
    constructor(): this(445.0, 325.0,25587, "Windfury Totem")

    override val id: Int = abilityId
    override val name: String = abilityName

    override fun gcdMs(sim: SimIteration): Int = sim.totemGcd().toInt()

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    override fun resourceCost(sim: SimIteration): Double {
        val tf = sim.subject.klass.talents[TotemicFocus.name] as TotemicFocus?
        val tfRed = tf?.totemCostReduction() ?: 0.0

        val mq = sim.subject.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        return General.resourceCostReduction(baseManaCost, listOf(tfRed, mqRed))
    }

    val weaponBuff = object : Buff() {
        override val name: String = "Windfury Totem (Weapon)"
        override val durationMs: Int = 10000
        override val hidden: Boolean = true

        val wfTotemAbility = object : Ability() {
            override val id: Int = abilityId
            override val name: String = abilityName

            override fun gcdMs(sim: SimIteration): Int = 0

            override fun cast(sim: SimIteration) {
                // Apply talents
                val impWeaponTotems = sim.subject.klass.talents[ImprovedWeaponTotems.name] as ImprovedWeaponTotems?
                val extraAp = (baseApBonus * (impWeaponTotems?.windfuryTotemApMultiplier() ?: 1.0)).toInt()

                // Do attack
                val mh = sim.subject.gear.mainHand
                val attack = Melee.baseDamageRoll(sim, mh, extraAp)
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
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_REPLACED_AUTO_ATTACK_HIT,
                Trigger.MELEE_REPLACED_AUTO_ATTACK_CRIT
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

    // This is the hidden buff that refreshes the weapon buff on every server tick
    val totemBuff = object : Buff() {
        override val name: String = abilityName
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.AIR_TOTEM)
        override val hidden: Boolean = true

        val totemProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SERVER_TICK
            )
            override val type: Type = Type.STATIC

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                sim.addBuff(weaponBuff)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(totemProc)
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(totemBuff)
    }
}
