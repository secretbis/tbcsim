package character.classes.hunter.abilities

import character.Ability
import character.Buff
import character.Proc
import character.classes.hunter.talents.TheBeastWithin
import data.Constants
import data.model.Item
import mechanics.General
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class KillCommand : Ability() {
    companion object {
        const val name = "Kill Command"
        const val flagBuffName = "Kill Command (available)"

        // This is just a marker to tell the rotation that we can cast Kill Command
        val killCommandFlagBuff = object : Buff() {
            override val name: String = flagBuffName
            override val durationMs: Int = 3000 // TODO: 1 server tick?  Needs confirmation
            override val hidden: Boolean = true
        }
    }

    override val id: Int = 34026
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = 5000
    override val castableOnGcd: Boolean = true

    val baseCost = 75.0
    override fun resourceCost(sp: SimParticipant): Double {
        val tbwDiscount = if(sp.buffs[TheBeastWithin.name] != null) { 0.2 } else 0.0

        return General.resourceCostReduction(baseCost, listOf(tbwDiscount))
    }

    override fun available(sp: SimParticipant): Boolean {
        val hasTriggerBuff = sp.buffs[flagBuffName] != null
        return hasTriggerBuff && super.available(sp)
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (base)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // On crit, proc the buff that lets us use Kill Command
        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.RANGED_AUTO_CRIT,
                Trigger.RANGED_WHITE_CRIT,
                Trigger.RANGED_YELLOW_CRIT
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(killCommandFlagBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    val bonusDmg = 127.0
    override fun cast(sp: SimParticipant) {
        if(sp.pet?.mhAutoAttack != null) {
            val petItem = sp.pet.mhAutoAttack!!.item(sp.pet)
            val petDmg = Melee.baseDamageRoll(sp.pet, petItem) + bonusDmg
            val result = Melee.attackRoll(sp.pet, petDmg, petItem)

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = Constants.DamageType.PHYSICAL,
                isWhiteDamage = true,
                abilityName = name,
                amount = result.first,
                result = result.second,
            )
            sp.pet.logEvent(event)

            val triggerTypes = when(result.second) {
                EventResult.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                EventResult.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
                EventResult.MISS -> listOf(Proc.Trigger.MELEE_MISS)
                EventResult.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
                EventResult.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
                EventResult.BLOCK -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
                EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
                else -> null
            }

            if(triggerTypes != null) {
                sp.fireProc(listOf(Proc.Trigger.HUNTER_CAST_KILL_COMMAND), null, this, null)
                sp.pet.fireProc(triggerTypes, listOf(petItem), this, event)
            }
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
