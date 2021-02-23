package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class FlameSpessarite(id: Int, val prefix: Prefix) : Gem(id, "Flame Spessarite", "inv_misc_gem_flamespessarite_02.jpg", prefix, Color.ORANGE, Quality.UNCOMMON)
