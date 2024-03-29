package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class StarOfElune(id: Int, prefix: Prefix) : Gem(id, "Star of Elune", "inv_jewelcrafting_starofelune_03.jpg", prefix, Color.BLUE, Quality.RARE)
