package data

object Constants {
    enum class StatType(val id: Int) {
        MANA(0),
        HEALTH(1),
        AGILITY(3),
        STRENGTH(4),
        INTELLECT(5),
        SPIRIT(6),
        STAMINA(7),
        DEFENSE_SKILL_RATING(12),
        DODGE_RATING(13),
        PARRY_RATING(14),
        BLOCK_RATING(15),
        HIT_MELEE_RATING(16),
        HIT_RANGED_RATING(17),
        HIT_SPELL_RATING(18),
        CRIT_MELEE_RATING(19),
        CRIT_RANGED_RATING(20),
        CRIT_SPELL_RATING(21),
        HIT_TAKEN_MELEE_RATING(22),
        HIT_TAKEN_RANGED_RATING(23),
        HIT_TAKEN_SPELL_RATING(24),
        CRIT_TAKEN_MELEE_RATING(25),
        CRIT_TAKEN_RANGED_RATING(26),
        CRIT_TAKEN_SPELL_RATING(27),
        HASTE_MELEE_RATING(28),
        HASTE_RANGED_RATING(29),
        HASTE_SPELL_RATING(30),
        HIT_RATING(31),
        CRIT_RATING(32),
        HIT_TAKEN_RATING(33),
        CRIT_TAKEN_RATING(34),
        RESILIENCE_RATING(35),
        HASTE_RATING(36),
        EXPERTISE_RATING(37),
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
