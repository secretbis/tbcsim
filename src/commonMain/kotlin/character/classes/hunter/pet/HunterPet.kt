package character.classes.hunter.pet

import character.*
import character.classes.hunter.pet.abilities.BiteRank9
import character.classes.hunter.pet.abilities.ClawRank9
import character.classes.hunter.pet.buffs.*
import data.Constants
import data.model.Item
import data.model.ItemSet
import data.model.Socket
import data.model.SocketBonus

// petDamageMultiplier is the +10% or -10% or whatever modifier the specific pet type gets
abstract class HunterPet(petDamageMultiplier: Double) : Class(mapOf(), HunterPetSpec()) {
    companion object {
        fun makePetAttackItem(name: String, minDmg: Double, maxDmg: Double): Item {
            return object : Item() {
                override var isAutoGenerated: Boolean = false
                override var id: Int = -1
                override var name: String = name
                override var itemLevel: Int = 1
                override var quality: Int = 1
                override var icon: String = ""
                override var itemSet: ItemSet? = null
                override var inventorySlot: Int = 21
                override var itemClass: Constants.ItemClass? = null
                override var itemSubclass: Constants.ItemSubclass? = null
                override var minDmg: Double = minDmg
                override var maxDmg: Double = maxDmg
                override var speed: Double = 1.0
                override var stats: Stats = Stats()
                override var sockets: Array<Socket> = arrayOf()
                override var socketBonus: SocketBonus? = null
            }
        }
    }

    // TODO: These are base cat stats from a random pserver, needs updating from live
    override var baseStats: Stats = Stats(
        strength = 162,
        agility = 128,
        stamina = 373,
        intellect = 60,
        spirit = 99
    )

    override fun talentFromString(name: String, ranks: Int): Talent? {
        return null
    }

    override fun abilityFromString(name: String, item: Item?): Ability? {
        return when(name) {
            "Bite" -> BiteRank9()
            "Claw" -> ClawRank9()
            else -> null
        }
    }

    override var buffs: List<Buff> = listOf(
        PetAnimalHandler(),
        PetBaseDamage(petDamageMultiplier),
        PetCobraReflexes(),
        PetFerociousInspiration(),
        PetFerocity(),
        PetFocusRegen(),
        PetFrenzy(),
        PetHappiness(),
        PetSerpentsSwiftness(),
        PetUnleashedFury()
    )

    override val resourceType: MutableList<Resource.Type> = mutableListOf(Resource.Type.FOCUS)
    override var canDualWield: Boolean = false
    override var attackPowerFromAgility: Int = 0
    override var attackPowerFromStrength: Int = 2
    override val critPctPerAgility: Double = 1.0 / 25.5
    override val dodgePctPerAgility: Double = 1.0 / 25.0
    override val baseDodgePct: Double = 0.0
    override var rangedAttackPowerFromAgility: Int = 0

    override val baseMana: Int = 0
    override val baseSpellCritChance: Double = 0.0
}
