package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class Pyrestone(id: Int, prefix: Prefix) : Gem(id, "Pyrestone", "inv_jewelcrafting_pyrestone_02.jpg", prefix, Color.ORANGE, Quality.EPIC)
