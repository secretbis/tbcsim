package data

import data.gems.*
import data.items.ItemIndex
import data.model.Gem
import data.model.Item

// Wraps the auto-generated ItemIndex, and adds in manual overrides
object Items {
    fun byName(name: String): Item? {
        return items(name) ?: ItemIndex.byName(name)
    }

    private fun items(name: String): Item? {
        return when(name) {
            // TODO: Weird one-off dungeon gems.  Just sub an epic gem that's close for now.
            // Meta gems
            "Chaotic Skyfire Diamond" -> ChaoticSkyfireDiamond()
            "Relentless Earthstorm Diamond" -> RelentlessEarthstormDiamond()
            "Thundering Skyfire Diamond" -> ThunderingSkyfireDiamond()

            // Red gems
            "Bold Blood Garnet" -> BloodGarnet(Gem.Prefix.BOLD)
            "Bold Living Ruby" -> LivingRuby(Gem.Prefix.BOLD)
            "Bold Crimson Spinel" -> CrimsonSpinel(Gem.Prefix.BOLD)
            "Bright Blood Garnet" -> BloodGarnet(Gem.Prefix.BRIGHT)
            "Bright Living Ruby" -> LivingRuby(Gem.Prefix.BRIGHT)
            "Bright Crimson Spinel" -> CrimsonSpinel(Gem.Prefix.BRIGHT)
            "Delicate Blood Garnet" -> BloodGarnet(Gem.Prefix.DELICATE)
            "Delicate Living Ruby" -> LivingRuby(Gem.Prefix.DELICATE)
            "Delicate Crimson Spinel" -> CrimsonSpinel(Gem.Prefix.DELICATE)
            "Flashing Living Ruby" -> LivingRuby(Gem.Prefix.FLASHING)
            "Flashing Crimson Spinel" -> CrimsonSpinel(Gem.Prefix.FLASHING)
            "Runed Blood Garnet" -> BloodGarnet(Gem.Prefix.RUNED)
            "Runed Living Ruby" -> LivingRuby(Gem.Prefix.RUNED)
            "Runed Crimson Spinel" -> CrimsonSpinel(Gem.Prefix.RUNED)
            "Subtle Living Ruby" -> LivingRuby(Gem.Prefix.SUBTLE)
            "Subtle Crimson Spinel" -> CrimsonSpinel(Gem.Prefix.SUBTLE)
            "Teardrop Blood Garnet" -> BloodGarnet(Gem.Prefix.TEARDROP)
            "Teardrop Living Ruby" -> LivingRuby(Gem.Prefix.TEARDROP)
            "Teardrop Crimson Spinel" -> CrimsonSpinel(Gem.Prefix.TEARDROP)

            // Yellow gems
            "Brilliant Golden Draenite" -> GoldenDraenite(Gem.Prefix.BRILLIANT)
            "Brilliant Downstone" -> Dawnstone(Gem.Prefix.BRILLIANT)
            "Brilliant Lionseye" -> Lionseye(Gem.Prefix.BRILLIANT)
            "Gleaming Golden Draenite" -> GoldenDraenite(Gem.Prefix.GLEAMING)
            "Gleaming Downstone" -> Dawnstone(Gem.Prefix.GLEAMING)
            "Gleaming Lionseye" -> Lionseye(Gem.Prefix.GLEAMING)
            "Great Golden Draenite" -> GoldenDraenite(Gem.Prefix.GREAT)
            "Great Downstone" -> Dawnstone(Gem.Prefix.GREAT)
            "Great Lionseye" -> Lionseye(Gem.Prefix.GREAT)
            "Quick Downstone" -> Dawnstone(Gem.Prefix.QUICK)
            "Quick Lionseye" -> Lionseye(Gem.Prefix.QUICK)
            "Rigid Golden Draenite" -> GoldenDraenite(Gem.Prefix.RIGID)
            "Rigid Downstone" -> Dawnstone(Gem.Prefix.RIGID)
            "Rigid Lionseye" -> Lionseye(Gem.Prefix.RIGID)
            "Smooth Golden Draenite" -> GoldenDraenite(Gem.Prefix.SMOOTH)
            "Smooth Downstone" -> Dawnstone(Gem.Prefix.SMOOTH)
            "Smooth Lionseye" -> Lionseye(Gem.Prefix.SMOOTH)
            "Thick Golden Draenite" -> GoldenDraenite(Gem.Prefix.THICK)
            "Thick Downstone" -> Dawnstone(Gem.Prefix.THICK)
            "Thick Lionseye" -> Lionseye(Gem.Prefix.THICK)

            // Blue gems
            "Lustrous Azure Moonstone" -> AzureMoonstone(Gem.Prefix.LUSTROUS)
            "Lustrous Star of Elune" -> StarOfElune(Gem.Prefix.LUSTROUS)
            "Lustrous Empyrean Sapphire" -> EmpyreanSapphire(Gem.Prefix.LUSTROUS)
            "Solid Azure Moonstone" -> AzureMoonstone(Gem.Prefix.SOLID)
            "Solid Star of Elune" -> StarOfElune(Gem.Prefix.SOLID)
            "Solid Empyrean Sapphire" -> EmpyreanSapphire(Gem.Prefix.SOLID)
            "Sparkling Azure Moonstone" -> AzureMoonstone(Gem.Prefix.SPARKLING)
            "Sparkling Star of Elune" -> StarOfElune(Gem.Prefix.SPARKLING)
            "Sparkling Empyrean Sapphire" -> EmpyreanSapphire(Gem.Prefix.SPARKLING)
            "Stormy Azure Moonstone" -> AzureMoonstone(Gem.Prefix.STORMY)
            "Stormy Star of Elune" -> StarOfElune(Gem.Prefix.STORMY)
            "Stormy Empyrean Sapphire" -> EmpyreanSapphire(Gem.Prefix.STORMY)

            // Orange
            "Glinting Flame Spessarite" -> FlameSpessarite(Gem.Prefix.GLINTING)
            "Glinting Noble Topaz" -> NobleTopaz(Gem.Prefix.GLINTING)
            "Glinting Pyrestone" -> Pyrestone(Gem.Prefix.GLINTING)
            "Inscribed Flame Spessarite" -> FlameSpessarite(Gem.Prefix.INSCRIBED)
            "Inscribed Noble Topaz" -> NobleTopaz(Gem.Prefix.INSCRIBED)
            "Inscribed Pyrestone" -> Pyrestone(Gem.Prefix.INSCRIBED)
            "Luminous Flame Spessarite" -> FlameSpessarite(Gem.Prefix.LUMINOUS)
            "Luminous Noble Topaz" -> NobleTopaz(Gem.Prefix.LUMINOUS)
            "Luminous Pyrestone" -> Pyrestone(Gem.Prefix.LUMINOUS)
            "Potent Flame Spessarite" -> FlameSpessarite(Gem.Prefix.POTENT)
            "Potent Noble Topaz" -> NobleTopaz(Gem.Prefix.POTENT)
            "Potent Pyrestone" -> Pyrestone(Gem.Prefix.POTENT)
            "Reckless Noble Topaz" -> NobleTopaz(Gem.Prefix.RECKLESS)
            "Reckless Pyrestone" -> Pyrestone(Gem.Prefix.RECKLESS)
            "Veiled Flame Spessarite" -> FlameSpessarite(Gem.Prefix.VEILED)
            "Veiled Noble Topaz" -> NobleTopaz(Gem.Prefix.VEILED)
            "Veiled Pyrestone" -> Pyrestone(Gem.Prefix.VEILED)
            "Wicked Flame Spessarite" -> FlameSpessarite(Gem.Prefix.WICKED)
            "Wicked Noble Topaz" -> NobleTopaz(Gem.Prefix.WICKED)
            "Wicked Pyrestone" -> Pyrestone(Gem.Prefix.WICKED)

            // Green
            "Dazzling Deep Peridot" -> DeepPeridot(Gem.Prefix.DAZZLING)
            "Dazzling Talasite" -> Talasite(Gem.Prefix.DAZZLING)
            "Dazzling Seaspray Emerald" -> SeasprayEmerald(Gem.Prefix.DAZZLING)
            "Enduring Deep Peridot" -> DeepPeridot(Gem.Prefix.ENDURING)
            "Enduring Talasite" -> Talasite(Gem.Prefix.ENDURING)
            "Enduring Seaspray Emerald" -> SeasprayEmerald(Gem.Prefix.ENDURING)
            "Jagged Deep Peridot" -> DeepPeridot(Gem.Prefix.JAGGED)
            "Jagged Talasite" -> Talasite(Gem.Prefix.JAGGED)
            "Jagged Seaspray Emerald" -> SeasprayEmerald(Gem.Prefix.JAGGED)
            "Radiant Deep Peridot" -> DeepPeridot(Gem.Prefix.RADIANT)
            "Radiant Talasite" -> Talasite(Gem.Prefix.RADIANT)
            "Radiant Seaspray Emerald" -> SeasprayEmerald(Gem.Prefix.RADIANT)
            "Steady Talasite" -> Talasite(Gem.Prefix.STEADY)
            "Steady Seaspray Emerald" -> SeasprayEmerald(Gem.Prefix.STEADY)

            // Purple
            "Balanced Shadow Draenite" -> ShadowDraenite(Gem.Prefix.BALANCED)
            "Balanced Nightseye" -> Nightseye(Gem.Prefix.BALANCED)
            "Balanced Shadowsong Amethyst" -> ShadowsongAmethyst(Gem.Prefix.BALANCED)
            "Glowing Shadow Draenite" -> ShadowDraenite(Gem.Prefix.GLOWING)
            "Glowing Nightseye" -> Nightseye(Gem.Prefix.GLOWING)
            "Glowing Shadowsong Amethyst" -> ShadowsongAmethyst(Gem.Prefix.GLOWING)
            "Infused Shadow Draenite" -> ShadowDraenite(Gem.Prefix.INFUSED)
            "Infused Nightseye" -> Nightseye(Gem.Prefix.INFUSED)
            "Infused Shadowsong Amethyst" -> ShadowsongAmethyst(Gem.Prefix.INFUSED)
            "Purified Shadow Draenite" -> ShadowDraenite(Gem.Prefix.PURIFIED)
            "Purified Nightseye" -> Nightseye(Gem.Prefix.PURIFIED)
            "Purified Shadowsong Amethyst" -> ShadowsongAmethyst(Gem.Prefix.PURIFIED)
            "Royal Shadow Draenite" -> ShadowDraenite(Gem.Prefix.ROYAL)
            "Royal Nightseye" -> Nightseye(Gem.Prefix.ROYAL)
            "Royal Shadowsong Amethyst" -> ShadowsongAmethyst(Gem.Prefix.ROYAL)
            "Shifting Shadow Draenite" -> ShadowDraenite(Gem.Prefix.SHIFTING)
            "Shifting Nightseye" -> Nightseye(Gem.Prefix.SHIFTING)
            "Shifting Shadowsong Amethyst" -> ShadowsongAmethyst(Gem.Prefix.SHIFTING)
            "Sovereign Shadow Draenite" -> ShadowDraenite(Gem.Prefix.SOVEREIGN)
            "Sovereign Nightseye" -> Nightseye(Gem.Prefix.SOVEREIGN)
            "Sovereign Shadowsong Amethyst" -> ShadowsongAmethyst(Gem.Prefix.SOVEREIGN)

            else -> null
        }
    }
}
