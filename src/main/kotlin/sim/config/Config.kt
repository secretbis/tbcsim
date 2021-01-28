package sim.config

import character.*
import com.charleskorn.kaml.Yaml
import data.Items
import data.abilities.generic.GenericAbilities
import data.abilities.raid.RaidAbilities
import data.enchants.Enchants
import data.model.Gem
import data.model.Item
import mu.KotlinLogging
import sim.SimOptions
import sim.rotation.Criterion
import sim.rotation.Rotation
import sim.rotation.Rule
import java.io.File

class Config(
    val character: Character,
    val rotation: Rotation,
    val opts: SimOptions
) {
    companion object {
        val logger = KotlinLogging.logger {}

        fun fromYml(file: File): Config {
            val configFile = file.readText()
            val yml = Yaml.default.decodeFromString(ConfigYml.serializer(), configFile)
            val character = createCharacter(yml)
            val rotation = createRotation(yml, character)

            return Config(
                character,
                rotation,
                createOpts(yml)
            )
        }

        private fun createOpts(yml: ConfigYml): SimOptions {
            return SimOptions(
                durationMs = yml.simOpts?.durationMs ?: SimOptions.Defaults.durationMs,
                stepMs = yml.simOpts?.stepMs ?: SimOptions.Defaults.stepMs,
                latencyMs = yml.simOpts?.latencyMs ?: SimOptions.Defaults.latencyMs,
                iterations = yml.simOpts?.iterations ?: SimOptions.Defaults.iterations,
                targetLevel = yml.simOpts?.targetLevel ?: SimOptions.Defaults.targetLevel,
                targetArmor = yml.simOpts?.targetArmor ?: SimOptions.Defaults.targetArmor,
                allowParryAndBlock = yml.simOpts?.allowParryAndBlock ?: SimOptions.Defaults.allowParryAndBlock,
            )
        }

        private fun makeRules(rotationRuleYml: List<RotationRuleYml>?, character: Character, phase: Rotation.Phase): List<Rule> {
            return rotationRuleYml?.map {
                // Check names in the character class first, then check generics
                val ability = character.klass.abilityFromString(it.name) ?: GenericAbilities.byName(it.name)
                if(ability == null) {
                    logger.warn { "Could not find ability with name: ${it.name}" }
                    null
                } else {
                    val criteria = it.criteria?.map { data ->
                        val criterion = Criterion.fromString(data["type"], data)
                        if(criterion == null) {
                            logger.warn { "Could not find criterion with type: ${data["type"]}" }
                            null
                        } else criterion
                    }?.filterNotNull() ?: listOf()

                    Rule(
                        ability,
                        phase,
                        criteria
                    )
                }
            }?.filterNotNull() ?: listOf()
        }

        private fun createRotation(yml: ConfigYml, character: Character): Rotation {
            val precombatRules = makeRules(yml.rotation?.precombat, character, Rotation.Phase.PRECOMBAT)
            val combatRules = makeRules(yml.rotation?.combat, character, Rotation.Phase.COMBAT)

            // Only build Raid/Party rules from the collection of raid abilities
            val raidAndPartyAbilities: List<String> = (yml.raid?.buffs ?: listOf()) + (yml.raid?.debuffs ?: listOf()) + (yml.raid?.party?.buffs ?: listOf())
            val raidAndPartyRules = raidAndPartyAbilities.mapNotNull {
                val ability = RaidAbilities.byName(it)
                if(ability == null) {
                    logger.warn { "Could not find raid/party ability with name: $it" }
                    null
                } else {
                    Rule(
                        ability,
                        Rotation.Phase.RAID_OR_PARTY,
                        listOf()
                    )
                }
            }

            return Rotation(
                precombatRules + combatRules + raidAndPartyRules
            )
        }

        private fun createItemFromGear(itemYml: GearItemYml?): Item {
            return if(itemYml != null) {
                var item = Items.byName(itemYml.name)
                if(item == null) {
                    logger.warn { "Could not find item with name: ${itemYml.name}" }
                    item = Item()
                }

                if(itemYml.enchant != null) {
                    val enchant = Enchants.byName(itemYml.enchant, item)
                    if(enchant == null) {
                        logger.warn { "Could not find enchant with name: ${itemYml.name}" }
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
                    val gemName = itemYml.gems?.get(index)
                    val gem = if(gemName != null) { Items.byName(gemName) } else null
                    if(gem == null) {
                        logger.warn { "Could not find gem with name: ${gemName}" }
                    } else {
                        if(gem is Gem && socket.canSocket(gem)) {
                            socket.gem = gem
                        } else {
                            logger.warn { "Cannot socket item into socket: $gemName -> ${itemYml.name} #$index" }
                        }
                    }
                }

                item
            } else Item()
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
            val gear = Gear(
                mainHand = createItemFromGear(yml.gear?.mainHand),
                offHand = createItemFromGear(yml.gear?.offHand),
                rangedTotemLibram = createItemFromGear(yml.gear?.rangedLibramTotem),
                ammo = createItemFromGear(yml.gear?.ammo),
                head = createItemFromGear(yml.gear?.head),
                neck = createItemFromGear(yml.gear?.neck),
                shoulders = createItemFromGear(yml.gear?.shoulders),
                back = createItemFromGear(yml.gear?.back),
                chest = createItemFromGear(yml.gear?.chest),
                wrists = createItemFromGear(yml.gear?.wrists),
                hands = createItemFromGear(yml.gear?.hands),
                waist = createItemFromGear(yml.gear?.waist),
                legs = createItemFromGear(yml.gear?.legs),
                feet = createItemFromGear(yml.gear?.feet),
                ring1 = createItemFromGear(yml.gear?.ring1),
                ring2 = createItemFromGear(yml.gear?.ring2),
                trinket1 = createItemFromGear(yml.gear?.trinket1),
                trinket2 = createItemFromGear(yml.gear?.trinket2)
            )

            return Character(
                klass = characterClass,
                race = race,
                level = yml.level,
                gear = gear
            )
        }
    }
}
