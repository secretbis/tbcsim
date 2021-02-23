package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class GoldenDraenite(id: Int, val prefix: Prefix) : Gem(id, "Golden Draenite", "inv_misc_gem_goldendraenite_02.jpg", prefix, Color.YELLOW, Quality.UNCOMMON)
