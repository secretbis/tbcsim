package sim.config

import character.*
import com.charleskorn.kaml.Yaml
import data.enchants.Mongoose
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
                val item = Item()

                if(itemYml.enchant != null) {
                    item.enchant = Mongoose(item)
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
                offHand = createItemFromGear(yml.gear?.offHand)
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
