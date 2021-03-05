package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class BloodGarnet(id: Int, prefix: Prefix) : Gem(id, "Blood Garnet", "inv_misc_gem_bloodgem_02.jpg", prefix, Color.RED, Quality.UNCOMMON)
