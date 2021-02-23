package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class Nightseye(id: Int, val prefix: Prefix) : Gem(id, "Nightseye", "inv_jewelcrafting_nightseye_03.jpg", prefix, Color.PURPLE, Quality.RARE)
