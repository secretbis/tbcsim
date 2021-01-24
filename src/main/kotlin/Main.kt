import character.Character
import character.Gear
import character.classes.shaman.Shaman
import character.classes.shaman.buffs.WindfuryWeapon
import character.classes.shaman.talents.*
import character.races.Draenei
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import data.DB
import data.model.Item
import data.buffs.Mongoose
import kotlinx.coroutines.runBlocking
import sim.Sim
import sim.SimOptions
import sim.rotation.Rotation

fun testCharacter(): Character {
    val gear = Gear()
    gear.mainHand = Item()
    gear.mainHand.id = 1
    gear.mainHand.enchant = Mongoose(gear.mainHand)
    gear.mainHand.temporaryEnhancement = WindfuryWeapon(gear.mainHand)

    gear.offHand = Item()
    gear.offHand.id = 2
    gear.offHand.enchant = Mongoose(gear.offHand)
    gear.offHand.temporaryEnhancement = WindfuryWeapon(gear.offHand)

    return Character(
        klass = Shaman(
            talents = mapOf(
                DualWield.name to DualWield(1),
                DualWieldSpecialization.name to DualWieldSpecialization(3),
                Flurry.name to Flurry(5),
                NaturesGuidance.name to NaturesGuidance(3),
                WeaponMastery.name to WeaponMastery(5)
            )
        ),
        race = Draenei(),
        gear = gear
    )
}

fun testRotation(): Rotation {
    return Rotation(
        listOf()
    )
}

fun simOpts(): SimOptions {
    return SimOptions(iterations = 1000)
}

fun setupLogging() {
    val logKey = org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY
    if(System.getProperty(logKey).isNullOrEmpty()) {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")
    }
}

class TBCSim : CliktCommand() {
    val generate: Boolean by option("-g", "--generate", help="Autogenerate item classes").flag(default = false)

    override fun run() {
        setupLogging()

        if(generate) {
            DB.init()
        } else {
            runBlocking {
                Sim(
                    testCharacter(),
                    testRotation(),
                    simOpts()
                ).sim()
            }
        }
    }
}

fun main(args: Array<String>) = TBCSim().main(args)
