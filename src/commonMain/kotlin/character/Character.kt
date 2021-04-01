package character

import sim.rotation.Rotation
import kotlin.js.JsExport

@JsExport
open class Character(
    val klass: Class,
    val race: Race,
    val level: Int = 70,
    var gear: Gear = Gear(),
    val pet: Pet? = null,
    val petRotation: Rotation? = null,
    val subTypes: Set<CharacterType> = setOf()
)
