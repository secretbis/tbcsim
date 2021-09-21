package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class SovereignTanzanite : Gem(30546, "Sovereign Tanzanite", "inv_jewelcrafting_nightseye_03.jpg", null, Color.PURPLE, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        stamina = 6,
        strength = 5,
    )
}
