package character.classes.hunter.pet.buffs

import character.Buff
import character.Stats
import character.classes.hunter.talents.UnleashedFury
import sim.SimParticipant

class PetBaseDamage(val multiplier: Double) : Buff() {
    override val name: String = "Pet Damage Modifier"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    // TODO: How does the "base" "high" DPS pet buff interact with Unleashed Fury?
    //      For now, presume multiplicative
    override fun modifyStats(sp: SimParticipant): Stats {
        val unleashedFury = sp.owner?.character?.klass?.talents?.get(UnleashedFury.name) as UnleashedFury?
        val ufMultiplier = unleashedFury?.petDamageMultiplier() ?: 1.0
        val totalMultiplier = multiplier * ufMultiplier

        // Various stats scale with the owner
        val meleeAp = (sp.owner?.rangedAttackPower() ?: 0) * 0.22
        val spellDamage = (sp.owner?.rangedAttackPower()?: 0) * 0.125

        val stamina = (sp.owner?.stamina() ?: 0) * 0.3
        val armor = (sp.owner?.armor() ?: 0) * 0.35
        val fireResistance = sp.owner?.stats?.fireResistance ?: 0
        val frostResistance = sp.owner?.stats?.frostResistance ?: 0
        val natureResistance = sp.owner?.stats?.natureResistance ?: 0
        val shadowResistance = sp.owner?.stats?.shadowResistance ?: 0
        val arcaneResistance = sp.owner?.stats?.arcaneResistance ?: 0

        return Stats(
            physicalDamageMultiplier = totalMultiplier,
            spellDamageMultiplier = totalMultiplier,

            attackPower = meleeAp.toInt(),
            spellDamage = spellDamage.toInt(),

            stamina = stamina.toInt(),
            armor = armor.toInt(),
            fireResistance = fireResistance,
            frostResistance = frostResistance,
            natureResistance = natureResistance,
            shadowResistance = shadowResistance,
            arcaneResistance = arcaneResistance
        )
    }
}
