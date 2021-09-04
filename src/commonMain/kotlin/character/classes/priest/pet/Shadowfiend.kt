package character.classes.priest.pet

import character.*
import character.classes.priest.pet.abilities.*
import character.classes.priest.pet.buffs.*
import character.classes.priest.pet.ShadowfiendSpec
import data.Constants
import data.model.Item
import data.model.ItemSet
import data.model.Socket
import data.model.SocketBonus

class Shadowfiend : Class(mapOf(), ShadowfiendSpec()) {
    // https://web.archive.org/web/20071201221602/http://www.shadowpriest.com/viewtopic.php?t=7616
    // Derived from http://elitistjerks.com/506359-post39.html
    override val baseStats: Stats = Stats(
        armor = 5474,
        stamina = 280,
        intellect = 133,
        strength = 153,
        agility = 108,
        spirit = 122,
        attackPower = 286
    )

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return null
    }

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return null
    }
    

    override val buffs: List<Buff> = listOf(
        ShadowfiendBase(),
        ManaLeech()
    )

    override val resourceTypes: List<Resource.Type> = listOf(Resource.Type.MANA)
    override val canDualWield: Boolean = false
    override val attackPowerFromAgility: Int = 0
    override val attackPowerFromStrength: Int = 0
    override val critPctPerAgility: Double = 0.0
    override val dodgePctPerAgility: Double = 0.0
    override val baseDodgePct: Double = 0.0
    override val rangedAttackPowerFromAgility: Int = 0
    override val baseMana: Int = 2241
    override val baseSpellCritChance: Double = 0.0
}
