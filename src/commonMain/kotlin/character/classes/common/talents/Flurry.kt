package character.classes.common.talents

import character.*
import character.classes.shaman.Shaman
import character.classes.warrior.abilities.HeroicStrike
import data.itemsets.CataclysmHarness
import data.model.Item
import sim.Event
import sim.SimParticipant

class Flurry(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Flurry"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val chargeProc = fun(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_MISS,
                Trigger.MELEE_DODGE,
                Trigger.MELEE_PARRY,
                Trigger.MELEE_BLOCK,
                Trigger.MELEE_GLANCE
            )
            override val type: Type = Type.STATIC
            // Shaman Flurry stacks are not consumed on every hit - a small ICD is observed on the beta
            override fun cooldownMs(sp: SimParticipant): Int {
                return if(sp.character.klass is Shaman) 500 else 0
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeBuff(buff)
            }
        }
    }

    val hsProc = fun(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_YELLOW_HIT
            )
            override val type: Type = Type.STATIC
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                // TODO: Cleave
                if(ability?.name == HeroicStrike.name) {
                    sp.consumeBuff(buff)
                }
            }
        }
    }

    val hasteBuff = object : Buff() {
        override val name: String = "Flurry"
        override val icon: String = "ability_ghoulfrenzy.jpg"
        override val durationMs: Int = 15000
        override val maxCharges: Int = 3

        // Increase melee haste for as long as we have charges
        override fun modifyStats(sp: SimParticipant): Stats {
            val talentRanks = sp.character.klass.talents[Companion.name]?.currentRank ?: 0

            val state = state(sp)
            val modifier = if (talentRanks > 0 && state.currentCharges > 0) {
                1.0 + (0.05 * talentRanks)
            } else 1.0

            // If this is Shaman flurry, check the T5 bonus
            var shamanT5BonusHaste = 0.0
            if(sp.character.klass is Shaman) {
                if(sp.buffs[CataclysmHarness.FOUR_SET_BUFF_NAME] != null) {
                    shamanT5BonusHaste = CataclysmHarness.fourSetAdditionalFlurryHaste()
                }
            }

            return Stats(physicalHasteMultiplier = modifier + shamanT5BonusHaste)
        }

        val chargeProc = chargeProc(this)
        val hsProc = hsProc(this)

        // Proc off of melee auto hits to reduce our stacks
        override fun procs(sp: SimParticipant): List<Proc> = listOf(chargeProc, hsProc)
    }

    val onCritProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_CRIT
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(hasteBuff)
        }
    }

    val wrapper = object : Buff() {
        override val name: String = "Flurry (static)"
        override val icon: String = "ability_ghoulfrenzy.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun procs(sp: SimParticipant): List<Proc> = listOf(onCritProc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(wrapper)
}
