package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class DeepPeridot(id: Int, val prefix: Prefix) : Gem(id, "Deep Peridot", "inv_misc_gem_deepperidot_02.jpg", prefix, Color.GREEN, Quality.UNCOMMON)
