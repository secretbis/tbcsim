package data.gems

import character.Buff
import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class AssassinsFireOpal : Gem(30565, "Assassin's Fire Opal", "inv_jewelcrafting_nobletopaz_03.jpg", null, Color.ORANGE, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        meleeCritRating = 6.0,
        rangedCritRating = 6.0,
        dodgeRating = 5.0,
    )
}
