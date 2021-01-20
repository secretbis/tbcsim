package character

import mechanics.Rating
import sim.SimIteration

class Character(
    val klass: Class,
    val race: Race,
    val level: Int = 70,
    var gear: Gear = Gear(),
) {
    lateinit var stats: Stats
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
        return hasMainHandWeapon() && hasOffHandWeapon()
    }

    fun computeStats(sim: SimIteration, buffs: List<Buff>) {
        // Apply basic stats
        this.stats = Stats()
            .add(klass.baseStats)
            .add(race.baseStats)
            .add(gear.totalStats())
            .apply {
                // Apply flat modifiers from buffs
                buffs.filter { it.statModType == Buff.ModType.FLAT }.forEach {
                    it.modifyStats(sim ,this)
                }
            }
            .apply {
                // Apply percentage modifiers from buffs
                buffs.filter { it.statModType == Buff.ModType.PERCENTAGE }.forEach {
                    it.modifyStats(sim, this)
                }
            }
            .apply {
                // Apply percentage modifiers from buffs which are potentially dependent on other percentage modifiers
                buffs.filter { it.statModType == Buff.ModType.PERCENTAGE_OF_PERCENTAGE }.forEach {
                    it.modifyStats(sim, this)
                }
            }

        if(resource == null) {
            resource = Resource(this)
        }
    }

    fun getMeleeHitPct(): Double {
        return stats.physicalHitRating / Rating.meleeHitPerPct
    }

    fun getSpellHitPct(): Double {
        return stats.spellHitRating / Rating.spellHitPerPct
    }

    fun getExpertisePct(): Double {
        return stats.expertiseRating / Rating.expertisePerPct
    }

    fun getMeleeCritPct(): Double {
        return stats.physicalCritRating / Rating.critPerPct
    }

    fun getSpellCritPct(): Double {
        return stats.spellCritRating / Rating.critPerPct
    }

    fun getArmorPen(): Double {
        return stats.armorPen.toDouble()
    }

    fun getMeleeHastePct(): Double {
        // TODO: Buffs and etc
        return stats.physicalHasteRating / Rating.hastePerPct
    }

    fun getSpellHastePct(): Double {
        // TODO: Buffs and etc
        return stats.spellHasteRating / Rating.hastePerPct
    }

    fun getPhysicalGcd(): Double {
        return (gcdBaseMs / (1 + getMeleeHastePct())).coerceAtLeast(minGcdMs)
    }

    fun getSpellGcd(): Double {
        return gcdBaseMs / (1 + getSpellHastePct()).coerceAtLeast(minGcdMs)
    }
}
