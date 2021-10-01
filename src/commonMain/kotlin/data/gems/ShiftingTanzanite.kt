package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class ShiftingTanzanite : Gem(30549, "Shifting Tanzanite", "inv_jewelcrafting_nightseye_03.jpg", null, Color.PURPLE, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        stamina = 6,
        agility = 5,
    )
}
