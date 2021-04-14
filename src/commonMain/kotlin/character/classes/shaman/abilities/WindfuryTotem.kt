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
import sim.SimParticipant

open class WindfuryTotem(val baseApBonus: Double, val baseManaCost: Double, val abilityId: Int, val abilityName: String): Ability() {
    constructor(): this(445.0, 325.0,25587, "Windfury Totem")

    override val id: Int = abilityId
    override val name: String = abilityName

    override fun gcdMs(sp: SimParticipant): Int = sp.totemGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return true
    }

    override fun resourceCost(sp: SimParticipant): Double {
        val tf = sp.character.klass.talents[TotemicFocus.name] as TotemicFocus?
        val tfRed = tf?.totemCostReduction() ?: 0.0

        val mq = sp.character.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        return General.resourceCostReduction(baseManaCost, listOf(tfRed, mqRed))
    }

    val weaponBuff = object : Buff() {
        override val name: String = "$abilityName (Weapon)"
        override val durationMs: Int = 10000

        val wfTotemAbility = object : Ability() {
            override val id: Int = abilityId
            override val name: String = abilityName

            override fun gcdMs(sp: SimParticipant): Int = 0

            override fun cast(sp: SimParticipant) {
                // Apply talents
                val impWeaponTotems = sp.character.klass.talents[ImprovedWeaponTotems.name] as ImprovedWeaponTotems?
                val extraAp = (baseApBonus * (impWeaponTotems?.windfuryTotemApMultiplier() ?: 1.0)).toInt()

                // Do attack
                val mh = sp.character.gear.mainHand
                val attack = Melee.baseDamageRoll(sp, mh, extraAp)
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

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(weaponBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(totemProc)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(totemBuff)
    }
}
