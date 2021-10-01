package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class StalwartFireOpal : Gem(30554, "Stalwart Fire Opal", "inv_jewelcrafting_nobletopaz_03.jpg", null, Color.ORANGE, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        defenseRating = 5.0,
        dodgeRating = 4.0,
    )
}
