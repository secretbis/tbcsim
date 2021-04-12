package ep

import kotlinx.serialization.Serializable

@Serializable
data class EpOutputOptions(
    var benefitsFromMeleeWeaponDps: Boolean,
    var benefitsFromRangedWeaponDps: Boolean,
)

@Serializable
data class EpOutput(
    var categories: Map<String, Map<String, Map<String, Double>>>,
    var options: Map<String, EpOutputOptions>
)
