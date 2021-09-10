package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class JaggedChrysoprase : Gem(30602, "Jagged Chrysoprase", "inv_jewelcrafting_talasite_03.jpg", null, Color.GREEN, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        stamina = 6,
        meleeCritRating = 5.0,
        rangedCritRating = 5.0,
    )
}
