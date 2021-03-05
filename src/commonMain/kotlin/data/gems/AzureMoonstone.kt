package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class AzureMoonstone(id: Int, prefix: Prefix) : Gem(id, "Azure Moonstone", "inv_misc_gem_azuredraenite_02.jpg", prefix, Color.BLUE, Quality.UNCOMMON)
