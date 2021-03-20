package character.classes.pet

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

abstract class Pet(val owner: Character, override var baseStats: Stats=Stats()) : Class(mapOf()) {
    // This is the +10% or -10% or whatever modifier the specific pet type gets
    abstract val petDamageMultiplier: Double

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return null
    }

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return null
    }

    val baseDmgBuff = object : Buff() {
        override val name: String = "Pet Damage Modifier"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // TODO: How does the "base" "high" DPS pet buff interact with Unleashed Fury?
        //      For now, presume additive
//        override fun modifyStats(sp: SimParticipant): Stats {
//            val unleashedFury = owner.klass.talents[UnleashedFury.name]? as UnleashedFury?
//            val ufMultiplier = unleashedFury?.petDamageMultiplier() ?: 1.0
//
//            return Stats(
//                physicalDamageMultiplier = petDamageMultiplier,
//                spellDamageMultiplier = petDamageMultiplier
//            )
//        }
    }

    val baseFocusRegen: Double = 5.0
    val focusRegenProc = object : Buff() {
        override val name: String = "Focus Regen"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

//        val proc = object : Proc() {
//            override val triggers: List<Trigger> = listOf(
//                Trigger.SERVER_TICK
//            )
//            override val type: Type = Type.STATIC
//
//            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
//                val bestialDiscTalent = owner.klass.talents[BestialDiscipline.name]? as BestialDiscipline?
//                val bdMultiplier = bestialDiscTalent?.focusRegenMultiplier() ?: 1.0
//
//                val tickModifier: Double = sp.sim.serverTickMs / 1000.0
//                part.addResource((baseFocusRegen * tickModifier * bdMultiplier).toInt())
//            }
//        }
    }

    override var buffs: List<Buff> = listOf()
    override var resourceType: Resource.Type = Resource.Type.FOCUS
    override var canDualWield: Boolean = false
    override var attackPowerFromAgility: Int = 0
    override var attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override val dodgePctPerAgility: Double = 1.0 / 25.0
    override val baseDodgePct: Double = 0.0
    override var rangedAttackPowerFromAgility: Int = 0

    override val baseMana: Int = 0
    override val baseSpellCritChance: Double = 0.0
}
