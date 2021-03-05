package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class CrimsonSpinel(id: Int, prefix: Prefix) : Gem(id, "Crimson Spinel", "inv_jewelcrafting_crimsonspinel_02.jpg", prefix, Color.RED, Quality.EPIC)
