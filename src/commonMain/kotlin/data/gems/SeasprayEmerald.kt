package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class SeasprayEmerald(id: Int, prefix: Prefix) : Gem(id, "Seaspray Emerald", "inv_jewelcrafting_seasprayemerald_02.jpg", prefix, Color.GREEN, Quality.EPIC)
