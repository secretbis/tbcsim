package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.FocusedRage
import character.classes.warrior.talents.ImprovedShieldBlock
import character.classes.warrior.talents.ImprovedSlam
import data.Constants
import data.itemsets.OnslaughtArmor
import data.model.Item
import mechanics.Melee
import mechanics.Rating
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant
import kotlin.random.Random

class ShieldBlock : Ability() {
    companion object {
        const val name = "Shield Block"
    }

    override val id: Int = 2565
    override val name: String = Companion.name
    override val icon: String = "ability_defend.jpg"

    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd: Boolean = true
    override fun cooldownMs(sp: SimParticipant): Int = 5000

    override fun available(sp: SimParticipant): Boolean {
        val isDefensiveStance = sp.buffs[DefensiveStance.name] != null
        val hasShield = sp.character.gear.offHand.itemSubclass == Constants.ItemSubclass.SHIELD
        return isDefensiveStance && hasShield && super.available(sp)
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double {
        val focusedRageRanks = sp.character.klass.talents[FocusedRage.name]?.currentRank ?: 0
        return 10.0 - focusedRageRanks
    }

    override fun cast(sp: SimParticipant) {
        val impShieldBlock = sp.character.klass.hasTalentRanks(ImprovedShieldBlock.name)

        val buff = object : Buff() {
            override val name: String = Companion.name
            override val durationMs: Int = if(impShieldBlock) { 6000 } else 5000
            override val maxCharges: Int = if(impShieldBlock) { 2 } else 1

            val proc = makeProc(this)
            fun makeProc(buff: Buff): Proc {
                return object : Proc() {
                    override val triggers: List<Trigger> = listOf(
                        Trigger.INCOMING_MELEE_BLOCK
                    )
                    override val type: Type = Type.STATIC

                    override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                        sp.consumeBuff(buff)
                    }
                }
            }

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(
                    blockRating = 75 * Rating.blockPerPct
                )
            }

            override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
        }

        sp.addBuff(buff)
    }
}
