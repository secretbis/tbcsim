package character.classes.hunter.pet.buffs

import character.Buff
import character.Stats
import character.classes.hunter.talents.UnleashedFury
import sim.SimParticipant

class PetUnleashedFury : Buff() {
    override val name: String = "Pet Unleashed Fury"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val icon: String = "ability_bullrush.jpg"

    override fun modifyStats(sp: SimParticipant): Stats {
        val unleashedFury = sp.owner?.character?.klass?.talents?.get(UnleashedFury.name) as UnleashedFury?
        val ufMultiplier = unleashedFury?.petDamageMultiplier() ?: 1.0

        return Stats(
            physicalDamageMultiplier = ufMultiplier,
            spellDamageMultiplier = ufMultiplier
        )
    }
}
