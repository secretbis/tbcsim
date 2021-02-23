package data.gems

import data.model.Color
import data.model.Gem
import data.model.Prefix
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class ShadowsongAmethyst(id: Int, val prefix: Prefix) : Gem(id, "Shadowsong Amethyst", "inv_jewelcrafting_shadowsongamethyst_02.jpg", prefix, Color.PURPLE, Quality.EPIC)
