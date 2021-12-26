package character

enum class Mutex {
    NONE,
    AIR_TOTEM,
    FIRE_TOTEM,
    WATER_TOTEM,
    EARTH_TOTEM,
    BATTLE_ELIXIR,
    GUARDIAN_ELIXIR,
    POTION,
    FOOD,
    WEAPON_TEMP_ENH_MH,
    WEAPON_TEMP_ENH_OH,

    // Stacking buffs/debuffs
    DEBUFF_MAJOR_ARMOR,
    BUFF_EXPOSE_WEAKNESS,
    BUFF_FEROCIOUS_INSPIRATION,
    BUFF_FAERIE_FIRE,
    BUFF_SPIRIT,

    // Hunter
    BUFF_HUNTER_ASPECT,
    BUFF_HUNTERS_MARK,

    // Rogue
    BUFF_SLICE_AND_DICE,
    DEBUFF_RUPTURE_DOT,
    DEBUFF_EXPOSE_ARMOR,

    // Warrior
    BUFF_BATTLE_SHOUT,
    BUFF_WARRIOR_STANCE
}
