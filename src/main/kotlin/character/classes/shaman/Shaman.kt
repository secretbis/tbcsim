package character.classes.shaman

import character.*
import character.classes.shaman.talents.*

class Shaman : Class {

    override var baseStats: Stats = Stats(
        agility = 222,
        intellect = 180,
        strength = 108,
        stamina = 154,
        spirit = 135
    )

    override var abilities: List<Ability> = listOf()
    override var buffs: List<Buff> = listOf()
    override var talents: List<Talent> = listOf(
        AncestralKnowledge(0),
//        Concussion(0),
        Convection(0),
        ElementalDevastation(0),
        ElementalWeapons(0),
        Flurry(0),
//        ImprovedWeaponTotems(0),
        MentalQuickness(0),
        NaturesGuidance(0),
//        Reverberation(0),
//        ShamanisticFocus(0),
//        ShamanisticRage(0),
//        ThunderingStrikes(0),
//        TotemicFocus(0),
        UnleashedRage(0),
//        WeaponMastery(0)
    )
    override var procs: List<Proc> = listOf()

    override var resourceType: Resource.Type = Resource.Type.MANA
    override var baseResourceAmount: Int = 0

    override var canDualWield: Boolean = true
    override var allowAutoAttack: Boolean = true

    override var attackPowerFromAgility: Int = 0
    override var attackPowerFromStrength: Int = 2
    override var rangedAttackPowerFromAgility: Int = 1
}
