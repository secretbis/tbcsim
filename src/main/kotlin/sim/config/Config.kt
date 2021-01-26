package sim.config

import character.*
import com.charleskorn.kaml.Yaml
import data.enchants.Enchants
import data.items.ItemIndex
import data.model.Item
import mu.KotlinLogging
import sim.SimOptions
import sim.rotation.Rotation
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

            return Config(
                createCharacter(yml),
                createRotation(yml),
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

        private fun createRotation(yml: ConfigYml): Rotation {
            // TODO: Actually implement this
            return Rotation(listOf())
        }

        private fun createItemFromGear(itemYml: GearItemYml?): Item {

            return if(itemYml != null) {
                var item = ItemIndex.byName(itemYml.name)
//                var item = Item()
                if(item == null) {
                    logger.warn { "Could not find item with name: ${itemYml.name}" }
                    item = Item()
                }

                if(itemYml.enchant != null) {
                    val enchant = Enchants.byName(itemYml.enchant, item)
                    if(enchant == null) {
                        logger.warn { "Could not find enchant with name: ${itemYml.name}" }
                    }
                    item.enchant = enchant
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
            // TODO: Actually implement this
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
