package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class EffulgentChrysoprase : Gem(30594, "Effulgent Chrysoprase", "inv_jewelcrafting_talasite_03.jpg", null, Color.GREEN, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        manaPer5Seconds = 2,
        defenseRating = 5.0,
    )
}
