package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class Lionseye(id: Int, prefix: Prefix) : Gem(id, "Lionseye", "inv_jewelcrafting_lionseye_02.jpg", prefix, Color.YELLOW, Quality.RARE)
