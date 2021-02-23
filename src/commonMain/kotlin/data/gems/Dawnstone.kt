package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class Dawnstone(id: Int, val prefix: Prefix) : Gem(id, "Dawnstone", "inv_jewelcrafting_dawnstone_03.jpg", prefix, Color.YELLOW, Quality.RARE)
