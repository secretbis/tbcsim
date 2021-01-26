package character

import mechanics.Rating
import sim.SimIteration

class Character(
    val klass: Class,
    val race: Race,
    val level: Int = 70,
    var gear: Gear = Gear()
) {
    var stats: Stats = Stats()
    var resource: Resource? = null

    var gcdBaseMs: Double = 1500.0
    val minGcdMs: Double = 1000.0

    fun hasMainHandWeapon(): Boolean {
        return gear.mainHand.id != -1
    }

    fun hasOffHandWeapon(): Boolean {
        return gear.offHand.id != -1
    }

    fun isDualWielding(): Boolean {
        return klass.canDualWield && hasMainHandWeapon() && hasOffHandWeapon()
    }

    fun computeStats(sim: SimIteration, buffs: List<Buff>) {
        // Apply basic stats
        this.stats = Stats()
            .add(klass.baseStats)
            .add(race.baseStats)
            .add(gear.totalStats())
            .apply {
                buffs.forEach {
                    it.modifyStats(sim, this)
                }
            }

        if(resource == null) {
            resource = Resource(this)
        }
    }

    fun armor(): Int {
        return (stats.armor * stats.armorMultiplier).toInt()
    }

    fun attackPower(): Int {
        return (
            (
                stats.attackPower +
                stats.strength * klass.attackPowerFromStrength +
                stats.agility * klass.attackPowerFromAgility
            ) * stats.attackPowerMultiplier
        ).toInt()
    }

    fun rangedAttackPower(): Int {
        return (
            (
                stats.attackPower +
                stats.agility * klass.rangedAttackPowerFromAgility
            ) * stats.rangedAttackPowerMultiplier
        ).toInt()
    }

    fun spellDamage(): Int {
        return (stats.spellDamage * stats.spellDamageMultiplier).toInt()
    }

    fun meleeHitPct(): Double {
        return stats.physicalHitRating / Rating.meleeHitPerPct
    }

    fun spellHitPct(): Double {
        return stats.spellHitRating / Rating.spellHitPerPct
    }

    fun expertisePct(): Double {
        return stats.expertiseRating / Rating.expertisePerPct
    }

    fun meleeCritPct(): Double {
        return stats.physicalCritRating / Rating.critPerPct + stats.agility * klass.critPctPerAgility
    }

    fun spellCritPct(): Double {
        return stats.spellCritRating / Rating.critPerPct
    }

    fun armorPen(): Int {
        return stats.armorPen
    }

    fun meleeHasteMultiplier(): Double {
        return (1.0 + (stats.physicalHasteRating / Rating.hastePerPct / 100.0)) * stats.physicalHasteMultiplier
    }

    fun spellHasteMultiplier(): Double {
        return (1.0 + (stats.physicalHasteRating / Rating.hastePerPct / 100.0)) * stats.spellHasteMultiplier
    }

    fun physicalGcd(): Double {
        return (gcdBaseMs / meleeHasteMultiplier()).coerceAtLeast(minGcdMs)
    }

    fun spellGcd(): Double {
        return (gcdBaseMs / spellHasteMultiplier()).coerceAtLeast(minGcdMs)
    }

    fun totemGcd(): Double {
        // TODO: Confirm this will be the case in Classic TBC at launch
        return 1.0
    }
}
