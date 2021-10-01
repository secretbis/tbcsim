package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class BeamingFireOpal : Gem(30601, "Beaming Fire Opal", "inv_jewelcrafting_nobletopaz_03.jpg", null, Color.ORANGE, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        dodgeRating = 5.0,
        resilienceRating = 4.0,
    )
}
