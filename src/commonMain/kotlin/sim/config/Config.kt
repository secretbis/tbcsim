package sim.config

import character.Character
import sim.rotation.Rotation
import kotlin.js.JsExport

@JsExport
data class Config(
    val character: Character,
    val rotation: Rotation,
    val petCharacter: Character? = null,
    val petRotation: Rotation? = null
)
