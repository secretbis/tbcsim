package character.classes.priest

import character.*
import character.classes.priest.abilities.*
import data.model.Item

class Priest(talents: Map<String, Talent>, spec: Spec) : Class(talents, spec) {
    override val baseStats: Stats = Stats(
        agility = 184,
        intellect = 180,
        strength = 146,
        stamina = 154,
        spirit = 135
    )
    override val buffs: List<Buff> = listOf()

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            MindBlast.name -> MindBlast()
            ShadowWordDeath.name -> ShadowWordDeath()
            ShadowWordPain.name -> ShadowWordPain()
            VampiricTouch.name -> VampiricTouch()
            else -> null
        }
    }

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return when(name) {
            else -> null
        }
    }

    override val resourceTypes: List<Resource.Type> = listOf(Resource.Type.MANA)
    override val canDualWield: Boolean = false;
    override val attackPowerFromAgility: Int = 0
    override val attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override val rangedAttackPowerFromAgility: Int = 0
    override val baseMana: Int = 2620
    // https://worldofwarcraft.fandom.com/et/wiki/Spell_critical_strike
    override val baseSpellCritChance: Double = 1.24
    override val dodgePctPerAgility: Double = 1.0 / 25.0
    override val baseDodgePct: Double = 3.18
}
