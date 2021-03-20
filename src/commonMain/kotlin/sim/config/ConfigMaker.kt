package sim.config

import character.Character
import character.Class
import character.Gear
import character.Race
import data.Items
import data.abilities.generic.GenericAbilities
import data.abilities.raid.RaidAbilities
import data.Enchants
import data.itemscustom.EmptyItem
import data.model.Gem
import data.model.Item
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import net.mamoe.yamlkt.Yaml
import sim.rotation.Criterion
import sim.rotation.Rotation
import sim.rotation.Rule
import kotlin.js.JsExport

@JsExport
object ConfigMaker {
    val logger = KotlinLogging.logger {}

    fun fromYml(ymlText: String): Config {
        val cfg = Yaml.decodeFromString(ConfigYml.serializer(), ymlText)
        val character = createCharacter(cfg)
        val rotation = createRotation(cfg, character)

        return Config(
            character,
            rotation
        )
    }

    fun fromJson(jsonText: String): Config {
        val cfg = Json.decodeFromString(ConfigYml.serializer(), jsonText)
        val character = createCharacter(cfg)
        val rotation = createRotation(cfg, character)

        return Config(
            character,
            rotation
        )
    }

    private fun makeRules(rotationRuleYml: List<RotationRuleYml>?, character: Character, phase: Rotation.Phase): List<Rule> {
        return rotationRuleYml?.mapNotNull {
            // Check names in the character class first, then check generics
            val ability = character.klass.abilityFromString(it.name) ?: character.race.racialByName(it.name) ?: GenericAbilities.byName(it.name)
            if(ability == null) {
                logger.warn { "Could not find ability with name: ${it.name}" }
                null
            } else {
                val criteria = it.criteria?.mapNotNull { data ->
                    val criterion = Criterion.fromString(data.type, data)
                    if(criterion == null) {
                        logger.warn { "Could not find criterion with type: ${data.type}" }
                        null
                    } else criterion
                } ?: listOf()

                Rule(
                    ability,
                    phase,
                    criteria,
                    it.options
                )
            }
        } ?: listOf()
    }

    private fun createRotation(yml: ConfigYml, character: Character): Rotation {
        val precombatRules = makeRules(yml.rotation?.precombat, character, Rotation.Phase.PRECOMBAT)
        val combatRules = makeRules(yml.rotation?.combat, character, Rotation.Phase.COMBAT)

        // Only build Raid/Party rules from the collection of raid abilities
        val raidAndPartyAbilities: List<String> = (yml.raidBuffs ?: listOf()) + (yml.raidDebuffs ?: listOf())
        val raidAndPartyRules = raidAndPartyAbilities.mapNotNull {
            val ability = RaidAbilities.byName[it]
            if(ability == null) {
                logger.warn { "Could not find raid/party ability with name: $it" }
                null
            } else {
                Rule(
                    ability,
                    Rotation.Phase.RAID_OR_PARTY,
                    listOf(),
                    null
                )
            }
        }

        return Rotation(
            precombatRules + combatRules + raidAndPartyRules,
            yml.rotation?.autoAttack ?: true
        )
    }

    private fun createItemFromGear(itemYml: GearItemYml?, equippedSlot: String): Item {
        return if(itemYml != null) {
            var item: Item? = Items.byName[itemYml.name]?.invoke()
            if(item == null) {
                logger.warn { "Could not find item with name: ${itemYml.name}" }
                item = EmptyItem()
            }

            item.equippedSlot = equippedSlot

            if(itemYml.enchant != null) {
                val enchant = Enchants.byName[itemYml.enchant]?.invoke(item)
                if(enchant == null) {
                    logger.warn { "Could not find enchant with name: ${itemYml.enchant}" }
                } else {
                    item.enchant = enchant
                }
            }

            // Fill sockets
            if(item.sockets.size != itemYml.gems?.size ?: 0) {
                // Check that all sockets are filled
                logger.warn { "Too many or too few gems specified for item: ${itemYml.name}" }
            }

            item.sockets.forEachIndexed { index, socket ->
                val gemName = itemYml.gems?.getOrNull(index)
                val gem: Item? = if(gemName != null) { Items.byName[gemName]?.invoke() } else null
                if(gem == null) {
                    logger.warn { "Could not find gem with name: $gemName" }
                } else {
                    if(gem is Gem && socket.canSocket(gem)) {
                        socket.gem = gem
                    } else {
                        logger.warn { "Cannot socket item into socket: $gemName -> ${itemYml.name} #$index" }
                    }
                }
            }

            item
        } else EmptyItem()
    }

    private fun createCharacter(yml: ConfigYml): Character {
        // Class
        val characterClass = Class.fromString(yml.`class`)
            ?: throw IllegalArgumentException("Unknown character class: ${yml.`class`}")

        // Race
        val race = Race.fromString(yml.race)
            ?: throw IllegalArgumentException("Unknown character race: ${yml.race}")

        // Talents
        val talents = yml.talents?.mapNotNull {
            val talent = characterClass.talentFromString(it.name, it.rank)
            if(talent == null) {
                logger.warn { "Unknown character talent: ${it.name}"}
                null
            } else {
                Pair(it.name, talent)
            }
        }?.associate { Pair(it.first, it.second) } ?: mapOf()

        characterClass.talents = talents

        // Gear
        val gear = Gear()
        gear.mainHand = createItemFromGear(yml.gear?.mainHand, "mainHand")
        gear.offHand = createItemFromGear(yml.gear?.offHand, "offHand")
        gear.rangedTotemLibram = createItemFromGear(yml.gear?.rangedLibramTotem, "rangedLibramTotem")
        gear.ammo = createItemFromGear(yml.gear?.ammo, "ammo")
        gear.head = createItemFromGear(yml.gear?.head, "head")
        gear.neck = createItemFromGear(yml.gear?.neck, "neck")
        gear.shoulders = createItemFromGear(yml.gear?.shoulders, "shoulders")
        gear.back = createItemFromGear(yml.gear?.back, "back")
        gear.chest = createItemFromGear(yml.gear?.chest, "chest")
        gear.wrists = createItemFromGear(yml.gear?.wrists, "wrists")
        gear.hands = createItemFromGear(yml.gear?.hands, "hands")
        gear.waist = createItemFromGear(yml.gear?.waist, "waist")
        gear.legs = createItemFromGear(yml.gear?.legs,"legs")
        gear.feet = createItemFromGear(yml.gear?.feet, "feet")
        gear.ring1 = createItemFromGear(yml.gear?.ring1, "ring1")
        gear.ring2 = createItemFromGear(yml.gear?.ring2, "ring2")
        gear.trinket1 = createItemFromGear(yml.gear?.trinket1, "trinket1")
        gear.trinket2 = createItemFromGear(yml.gear?.trinket2, "trinket2")

        // Check that meta gem is active, warn if not
        if(!gear.metaGemActive()) {
            logger.warn { "Meta gem is not active - consider adjusting your gems in gear" }
        }

        return Character(
            klass = characterClass,
            race = race,
            level = yml.level,
            gear = gear
        )
    }
}
