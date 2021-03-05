package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class EmpyreanSapphire(id: Int, prefix: Prefix) : Gem(id, "Empyrean Sapphire", "inv_jewelcrafting_empyreansapphire_02.jpg", prefix, Color.BLUE, Quality.EPIC)
