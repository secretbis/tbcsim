package data

import data.gems.*
import data.items.ItemIndex
import data.itemscustom.Annihilator
import data.model.Prefix
import kotlin.js.JsExport

// Wraps the auto-generated ItemIndex, and adds in manual overrides
@JsExport
object Items {
    val items = arrayOf(
        // Manually added items
        { Annihilator() },

        // TODO: Weird one-off dungeon gems.  Just sub an epic gem that's close for now.
        // Meta gems
        { ChaoticSkyfireDiamond() },
        { RelentlessEarthstormDiamond() },
        { ThunderingSkyfireDiamond() },
        { SwiftSkyfireDiamond() },
        { DestructiveSkyfireDiamond() },
        { MysticalSkyfireDiamond() },
        { EnigmaticSkyfireDiamond() },
        { SwiftWindfireDiamond() },
        { SwiftStarfireDiamond() },
        { PotentUnstableDiamond() },
        { ImbuedUnstableDiamond() },
        { EmberSkyfireDiamond() },

        // Red gems
        { BloodGarnet(23095, Prefix.BOLD) },
        { LivingRuby(24027, Prefix.BOLD) },
        { CrimsonSpinel(32193, Prefix.BOLD) },
        { BloodGarnet(28595, Prefix.BRIGHT) },
        { LivingRuby(24031, Prefix.BRIGHT) },
        { CrimsonSpinel(32197, Prefix.BRIGHT) },
        { BloodGarnet(23097, Prefix.DELICATE) },
        { LivingRuby(24028, Prefix.DELICATE) },
        { CrimsonSpinel(32194, Prefix.DELICATE) },
        { LivingRuby(24036, Prefix.FLASHING) },
        { CrimsonSpinel(32199, Prefix.FLASHING) },
        { BloodGarnet(23096, Prefix.RUNED) },
        { LivingRuby(24030, Prefix.RUNED) },
        { CrimsonSpinel(32196, Prefix.RUNED) },
        { LivingRuby(24032, Prefix.SUBTLE) },
        { CrimsonSpinel(32198, Prefix.SUBTLE) },
        { BloodGarnet(23094, Prefix.TEARDROP) },
        { LivingRuby(24029, Prefix.TEARDROP) },
        { CrimsonSpinel(32195, Prefix.TEARDROP) },

        // Yellow gems
        { GoldenDraenite(23113, Prefix.BRILLIANT) },
        { Dawnstone(24047, Prefix.BRILLIANT) },
        { Lionseye(32204, Prefix.BRILLIANT) },
        { GoldenDraenite(23114, Prefix.GLEAMING) },
        { Dawnstone(24050, Prefix.GLEAMING) },
        { Lionseye(32207, Prefix.GLEAMING) },
        { GoldenDraenite(31860, Prefix.GREAT) },
        { Dawnstone(31861, Prefix.GREAT) },
        { Lionseye(32210, Prefix.GREAT) },
        { Dawnstone(35315, Prefix.QUICK) },
        { Lionseye(35761, Prefix.QUICK) },
        { GoldenDraenite(23116, Prefix.RIGID) },
        { Dawnstone(24051, Prefix.RIGID) },
        { Lionseye(32206, Prefix.RIGID) },
        { GoldenDraenite(28290, Prefix.SMOOTH) },
        { Dawnstone(24048, Prefix.SMOOTH) },
        { Lionseye(32205, Prefix.SMOOTH) },
        { GoldenDraenite(23115, Prefix.THICK) },
        { Dawnstone(24052, Prefix.THICK) },
        { Lionseye(32208, Prefix.THICK) },

        // Blue gems
        { AzureMoonstone(23121, Prefix.LUSTROUS) },
        { StarOfElune(24037, Prefix.LUSTROUS) },
        { EmpyreanSapphire(32202, Prefix.LUSTROUS) },
        { AzureMoonstone(23118, Prefix.SOLID) },
        { StarOfElune(24033, Prefix.SOLID) },
        { EmpyreanSapphire(32200, Prefix.SOLID) },
        { AzureMoonstone(23119, Prefix.SPARKLING) },
        { StarOfElune(24035, Prefix.SPARKLING) },
        { EmpyreanSapphire(32201, Prefix.SPARKLING) },
        { AzureMoonstone(23120, Prefix.STORMY) },
        { StarOfElune(24039, Prefix.STORMY) },
        { EmpyreanSapphire(32203, Prefix.STORMY) },

        // Orange
        { FlameSpessarite(23100, Prefix.GLINTING) },
        { NobleTopaz(24061, Prefix.GLINTING) },
        { Pyrestone(32220, Prefix.GLINTING) },
        { FlameSpessarite(23098, Prefix.INSCRIBED) },
        { NobleTopaz(24058, Prefix.INSCRIBED) },
        { Pyrestone(32217, Prefix.INSCRIBED) },
        { FlameSpessarite(23099, Prefix.LUMINOUS) },
        { NobleTopaz(24060, Prefix.LUMINOUS) },
        { Pyrestone(32219, Prefix.LUMINOUS) },
        { FlameSpessarite(23101, Prefix.POTENT) },
        { NobleTopaz(24059, Prefix.POTENT) },
        { Pyrestone(32218, Prefix.POTENT) },
        { NobleTopaz(35316, Prefix.RECKLESS) },
        { Pyrestone(35760, Prefix.RECKLESS) },
        { FlameSpessarite(31866, Prefix.VEILED) },
        { NobleTopaz(31867, Prefix.VEILED) },
        { Pyrestone(32221, Prefix.VEILED) },
        { FlameSpessarite(31869, Prefix.WICKED) },
        { NobleTopaz(31868, Prefix.WICKED) },
        { Pyrestone(32222, Prefix.WICKED) },

        // Green
        { DeepPeridot(23106, Prefix.DAZZLING) },
        { Talasite(24065, Prefix.DAZZLING) },
        { SeasprayEmerald(32225, Prefix.DAZZLING) },
        { DeepPeridot(23105, Prefix.ENDURING) },
        { Talasite(24062, Prefix.ENDURING) },
        { SeasprayEmerald(32223, Prefix.ENDURING) },
        { DeepPeridot(23104, Prefix.JAGGED) },
        { Talasite(24067, Prefix.JAGGED) },
        { SeasprayEmerald(32226, Prefix.JAGGED) },
        { DeepPeridot(23103, Prefix.RADIANT) },
        { Talasite(24066, Prefix.RADIANT) },
        { SeasprayEmerald(32224, Prefix.RADIANT) },
        { Talasite(33782, Prefix.STEADY) },
        { SeasprayEmerald(35758, Prefix.STEADY) },

        // Purple
        { ShadowDraenite(31862, Prefix.BALANCED) },
        { Nightseye(31863, Prefix.BALANCED) },
        { ShadowsongAmethyst(32213, Prefix.BALANCED) },
        { ShadowDraenite(23108, Prefix.GLOWING) },
        { Nightseye(24056, Prefix.GLOWING) },
        { ShadowsongAmethyst(32215, Prefix.GLOWING) },
        { ShadowDraenite(31864, Prefix.INFUSED) },
        { Nightseye(31865, Prefix.INFUSED) },
        { ShadowsongAmethyst(32214, Prefix.INFUSED) },
        { ShadowsongAmethyst(37503, Prefix.PURIFIED) },
        { ShadowDraenite(23109, Prefix.ROYAL) },
        { Nightseye(24057, Prefix.ROYAL) },
        { ShadowsongAmethyst(32216, Prefix.ROYAL) },
        { ShadowDraenite(23110, Prefix.SHIFTING) },
        { Nightseye(24055, Prefix.SHIFTING) },
        { ShadowsongAmethyst(32212, Prefix.SHIFTING) },
        { ShadowDraenite(23111, Prefix.SOVEREIGN) },
        { Nightseye(24054, Prefix.SOVEREIGN) },
        { ShadowsongAmethyst(32211, Prefix.SOVEREIGN) },
    ) + ItemIndex.items

    val byName = items.map { val eval = it(); eval.name to { it() } }.toMap()
    val byId = items.map { val eval = it(); eval.id to { it() } }.toMap()
    val bySlot = items.groupBy { val eval = it(); eval.inventorySlot }.mapValues { it.value.toTypedArray() }
}
