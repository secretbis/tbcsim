package data

object Constants {
    // These values are ordinal
    enum class StatType {
        NONE,
        MANA,
        HEALTH,
        AGILITY,
        STRENGTH,
        INTELLECT,
        SPIRIT,
        STAMINA,
        DEFENSE_SKILL_RATING,
        DODGE_RATING,
        PARRY_RATING,
        BLOCK_RATING,
        HIT_MELEE_RATING,
        HIT_RANGED_RATING,
        HIT_SPELL_RATING,
        CRIT_MELEE_RATING,
        CRIT_RANGED_RATING,
        CRIT_SPELL_RATING,
        HIT_TAKEN_MELEE_RATING,
        HIT_TAKEN_RANGED_RATING,
        HIT_TAKEN_SPELL_RATING,
        CRIT_TAKEN_MELEE_RATING,
        CRIT_TAKEN_RANGED_RATING,
        CRIT_TAKEN_SPELL_RATING,
        HASTE_MELEE_RATING,
        HASTE_RANGED_RATING,
        HASTE_SPELL_RATING,
        HIT_RATING,
        CRIT_RATING,
        HIT_TAKEN_RATING,
        CRIT_TAKEN_RATING,
        RESILIENCE_RATING,
        HASTE_RATING,
        EXPERTISE_RATING,
    }

    // These values are bitmasks
    enum class AllowableClass(val mask: Int) {
        WARRIOR(1),
        PALADIN(2),
        HUNTER(4),
        ROGUE(8),
        PRIEST(16),
        DEATHKNIGHT(32),
        SHAMAN(64),
        MAGE(128),
        WARLOCK(256),
        DRUID(1024)
    }

    // These values are ordinal
    enum class DamageType {
        PHYSICAL,
        HOLY,
        FIRE,
        NATURE,
        FROST,
        SHADOW,
        ARCANE,
    }
}
