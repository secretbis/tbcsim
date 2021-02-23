package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class Talasite(id: Int, val prefix: Prefix) : Gem(id, "Talasite", "inv_jewelcrafting_talasite_03.jpg", prefix, Color.GREEN, Quality.RARE)
