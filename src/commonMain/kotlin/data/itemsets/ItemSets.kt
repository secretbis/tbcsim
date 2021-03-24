package data.itemsets

import data.model.ItemSet
import mu.KotlinLogging

object ItemSets {
    val logger = KotlinLogging.logger {}

    // Every item in this list must have a distinct class name
    val allItemSets = listOf(
        AbsolutionRegalia(),
        AldorRegalia(),
        ArcanoweaveVestments(),
        AssassinationArmor(),
        AvatarRegalia(),
        BeastLordArmor(),
        BurningRage(),
        CataclysmRegalia(),
        CataclysmHarness(),
        CorruptorRaiment(),
        CrystalforgeArmor(),
        CrystalforgeBattlegear(),
        CycloneHarness(),
        CycloneRegalia(),
        Deathmantle(),
        DemonStalkerArmor(),
        DesolationBattlegear(),
        DestroyerBattlegear(),
        DoomplateBattlegear(),
        KhoriumWard(),
        FaithInFelsteel(),
        FelstalkerArmor(),
        FuryOfTheNether(),
        GronnstalkersArmor(),
        ImbuedNetherweave(),
        IncarnateRegalia(),
        JusticarArmor(),
        JusticarBattlegear(),
        LatrosFlurry(),
        LightbringerBattlegear(),
        MaleficRaiment(),
        MalorneHarness(),
        MalorneRegalia(),
        ManaEtchedRegalia(),
        Netherblade(),
        NetherscaleArmor(),
        NetherstrikeArmor(),
        NordrassilHarness(),
        NordrassilRegalia(),
        OblivionRaiment(),
        OnslaughtBattlegear(),
        PrimalIntent(),
        PrimalMooncloth(),
        RiftStalkerArmor(),
        RighteousArmor(),
        SkyshatterHarness(),
        SkyshatterRegalia(),
        SlayersArmor(),
        SoulclothEmbrace(),
        SpellstrikeInfusion(),
        StrengthOfTheClefthoof(),
        TempestRegalia(),
        TheFistsOfFury(),
        TheTwinBladesOfAzzinoth(),
        TheTwinStars(),
        ThunderheartHarness(),
        ThunderheartRegalia(),
        TidefuryRaiment(),
        TirisfalRegalia(),
        VoidheartRaiment(),
        WarbringerBattlegear(),
        WastewalkerArmor(),
        WindhawkArmor(),
        WrathOfSpellfire()
    )

    val byIdMap = allItemSets.map { it.id to it }.toMap()

    // This is a list of item sets that are really just not relevant
    val ignoredSpellIds = setOf(
        521, // Dreamwalker Raiment
        523, // Dreadnaught's Battlegear
        524, // Bonescythe Armor
        525, // Vestments of Faith
        526, // Frostfire Regalia
        527, // The Earthshatterer
        528, // Redemption Armor
        529, // Plagueheart Raiment
        530, // Cryptstalker Armor
        553, // Frozen Shadoweave
        562, // Adamantite Battlegear
        563, // Enchanted Adamantite Armor
        564, // Flame Guard
        567, // Gladiator's Battlegear
        568, // Gladiator's Dreadgear
        570, // The Unyielding
        571, // Whitemend Wisdom
        572, // Battlecast Garb
        573, // Fel Skin
        577, // Gladiator's Vestments
        578, // Gladiator's Earthshaker
        579, // Gladiator's Regalia
        580, // Gladiator's Thunderfist
        581, // Gladiator's Raiment
        582, // Gladiator's Aegis
        583, // Gladiator's Vindication
        584, // Gladiator's Sanctuary
        585, // Gladiator's Wildhide
        586, // Gladiator's Pursuit
        587, // High Warlord's Aegis
        588, // High Warlord's Battlegear
        589, // Grand Marshal's Aegis
        590, // Grand Marshal's Battlegear
        591, // Grand Marshal's Dreadgear
        592, // High Warlord's Dreadgear
        593, // Grand Marshal's Earthshaker
        594, // High Warlord's Earthshaker
        595, // Grand Marshal's Pursuit
        596, // High Warlord's Pursuit
        597, // Grand Marshal's Raiment
        598, // High Warlord's Raiment
        599, // Grand Marshal's Regalia
        600, // High Warlord's Regalia
        601, // Grand Marshal's Sanctuary
        602, // High Warlord's Sanctuary
        603, // Grand Marshal's Thunderfist
        604, // High Warlord's Thunderfist
        605, // Grand Marshal's Vestments
        606, // High Warlord's Vestments
        607, // Grand Marshal's Vindication
        608, // High Warlord's Vindication
        609, // Grand Marshal's Wildhide
        610, // High Warlord's Wildhide
        615, // Gladiator's Felshroud
        624, // Justicar Raiment
        627, // Crystalforge Raiment
        631, // Cyclone Raiment
        634, // Cataclysm Raiment
        637, // Moonglade Raiment
        638, // Malorne Raiment
        642, // Nordrassil Raiment
        647, // Incanter's Regalia
        653, // Bold Armor
        662, // Hallowed Raiment
        663, // Incarnate Raiment
        654, // Warbringer Armor
        656, // Destroyer Armor
        665, // Avatar Raiment
        673, // Onslaught Armor
        675, // Vestments of Absolution
        678, // Thunderheart Raiment
        679, // Lightbringer Armor
        681, // Lightbringer Raiment
        683, // Skyshatter Raiment
        685, // Gladiator's Refuge
        686, // Gladiator's Wartide
        687, // Gladiator's Investiture
        688, // Grand Marshal's Refuge
        689, // High Warlord's Refuge
        690, // Gladiator's Redemption
        691, // Grand Marshal's Investiture
        692, // High Warlord's Investiture
        693, // Grand Marshal's Redemption
        694, // High Warlord's Redemption
        695, // Grand Marshal's Wartide
        696, // High Warlord's Wartide
        738, // Dreadweave Battlegear
        739, // Mooncloth Battlegear
        740, // Satin Battlegear
        741, // Evoker's Silk Battlegear
        742, // Dragonhide Battlegear
        743, // Wyrmhide Battlegear
        744, // Kodohide Battlegear
        745, // Opportunist's Battlegear
        746, // Seer's Mail Battlegear
        747, // Seer's Ringmail Battlegear
        748, // Seer's Linked Battlegear
        749, // Stalker's Chain Battlegear
        750, // Savage Plate Battlegear
        751, // Crusader's Ornamented Battlegear
        752, // Crusader's Scaled Battlegear
    )

    fun byId(id: Int): ItemSet? {
        val set = byIdMap[id]

        if(set == null && !ignoredSpellIds.contains(id)) {
            logger.warn { "Unknown item set ID: $id" }
        }

        return set
    }

    init {
        // Double check that there are no inadvertent ID collisions
        val found = mutableSetOf<Int>()
        allItemSets.map { it.id }.forEach { id ->
            if(found.contains(id)) {
                logger.warn { "Duplicate ItemSet ID detected: $id" }
            }
            found.add(id)
        }
    }
}
