package sim.target

import character.Buff
import character.CharacterType
import character.classes.boss.buffs.MorogrimTidewalkerThrash
import sim.SimOptions
import sim.rotation.Rotation

object TargetProfiles {
    data class ProfileResult(
        val name: String,
        val simOptions: SimOptions?,
        val buffs: List<Buff> = listOf(),
        val rotation: Rotation? = null
    )

    // Loads various boss configurations by name
    // Most data from:
    // https://discord.com/channels/383596811517952002/814527236241620993/879524912774131753
    fun profileFor(name: String): ProfileResult? {
        // TODO: Implement various boss abilities
        return when(name.toLowerCase()) {
            "the lurker below" -> {
                val opts = SimOptions(
                    targetType = CharacterType.BEAST.ordinal,
                    targetWeaponPower = 12365,
                    targetAutoAttackSpeedMs = 2000
                )

                ProfileResult("The Lurker Below", opts)
            }
            "leotheras the blind" -> {
                val opts = SimOptions(
                    targetType = CharacterType.HUMANOID.ordinal,
                    targetWeaponPower = 7419,
                    targetAutoAttackSpeedMs = 1800,
                    targetDualWield = true
                )

                ProfileResult("Leotheras the Blind", opts)
            }
            "morogrim tidewalker" -> {
                val opts = SimOptions(
                    targetType = CharacterType.GIANT.ordinal,
                    targetWeaponPower = 13190,
                    targetAutoAttackSpeedMs = 1600
                )

                val buffs = listOf(MorogrimTidewalkerThrash())

                ProfileResult("Morogrim Tidewalker", opts, buffs)
            }
            else -> null
        }
    }
}