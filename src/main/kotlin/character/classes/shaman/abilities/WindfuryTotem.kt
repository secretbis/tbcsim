package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import character.classes.shaman.talents.ImprovedWeaponTotems
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

class WindfuryTotem: Ability() {
    companion object {
        const val name = "Windfury Totem"
    }

    override val id: Int = 25587
    override val name: String = Companion.name

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    val weaponBuff = object : Buff() {
        override val name: String = "Windfury Totem (Weapon)"
        override val durationMs: Int = 10000
        override val hidden: Boolean = true

        val wfTotemAbility = object : Ability() {
            val baseExtraAp = 445
            override val id: Int = 15497
            override val name: String = "Windfury Totem"

            override fun gcdMs(sim: SimIteration): Int = 0

            override fun cast(sim: SimIteration, free: Boolean) {
                // Apply talents
                val impWeaponTotems = sim.subject.klass.talents[ImprovedWeaponTotems.name] as ImprovedWeaponTotems?
                val extraAp = (baseExtraAp * (impWeaponTotems?.windfuryTotemApMultiplier() ?: 1.0)).toInt()

                // Do attack
                val mh = sim.subject.gear.mainHand
                val attack = Melee.baseDamageRoll(sim, mh, extraAp)
                val result = Melee.attackRoll(sim, attack, isWhiteDmg = true, isOffHand = false)

                sim.logEvent(
                    Event(
                        eventType = Event.Type.DAMAGE,
                        damageType = Constants.DamageType.PHYSICAL,
                        abilityName = name,
                        amount = result.first,
                        result = result.second,
                    )
                )

                // Proc anything that can proc off a white hit
                // TODO: Should I fire procs off miss/dodge/parry/etc?
                val triggerTypes = when(result.second) {
                    Event.Result.HIT -> listOf(Proc.Trigger.MELEE_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                    Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
                    else -> null
                }

                if(triggerTypes != null) {
                    sim.fireProc(triggerTypes, listOf(mh), this)
                }
            }

            override val baseCastTimeMs: Int = 0
        }

        val weaponProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                // TODO: Per random internet forums, this only procs off autos
                //       Needs confirmation
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
            )

            override val type: Type = Type.PERCENT
            override val percentChance: Double = 20.0

            override fun shouldProc(sim: SimIteration, items: List<Item>?, ability: Ability?): Boolean {
                val isMhWeapon = items?.first() === sim.subject.gear.mainHand
                val mhHasNoTempEnh = sim.subject.gear.mainHand.temporaryEnhancement == null
                return isMhWeapon && mhHasNoTempEnh && super.shouldProc(sim, items, ability)
            }

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                wfTotemAbility.cast(sim)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(weaponProc)
    }

    // This is the hidden buff that refreshes the weapon buff on every server tick
    val totemBuff = object : Buff() {
        override val name: String = "Windfury Totem"
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.AIR_TOTEM)

        val totemProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SERVER_TICK
            )
            override val type: Type = Type.STATIC

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                sim.addBuff(weaponBuff)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(totemProc)
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(totemBuff)
    }

    override val baseCastTimeMs: Int = 0
    override fun gcdMs(sim: SimIteration): Int = sim.subject.totemGcd().toInt()
}
