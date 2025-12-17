package sim.config

import character.Character
import kotlin.js.JsExport
import sim.rotation.Rotation

@JsExport
data class Config(
    val character: Character,
    val rotation: Rotation,
    val petCharacter: Character? = null,
    val petRotation: Rotation? = null,
)
