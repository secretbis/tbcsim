package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class ShadowDraenite(id: Int, val prefix: Prefix) : Gem(id,"Shadow Draenite", "inv_misc_gem_ebondraenite_02.jpg", prefix, Color.PURPLE, Quality.UNCOMMON)
