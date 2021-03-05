package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class NobleTopaz(id: Int, prefix: Prefix) : Gem(id, "Noble Topaz", "inv_jewelcrafting_nobletopaz_03.jpg", prefix, Color.ORANGE, Quality.RARE)
