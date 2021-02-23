package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class LivingRuby(id: Int, val prefix: Prefix) : Gem(id, "Living Ruby", "inv_jewelcrafting_livingruby_03.jpg", prefix, Color.RED, Quality.RARE)
