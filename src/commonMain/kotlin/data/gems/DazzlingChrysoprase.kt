package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class DazzlingChrysoprase : Gem(30589, "Dazzling Chrysoprase", "inv_jewelcrafting_talasite_03.jpg", null, Color.GREEN, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        intellect = 5,
        manaPer5Seconds = 2,
    )
}
