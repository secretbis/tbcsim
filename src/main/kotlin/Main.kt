import character.Character
import character.Gear
import character.classes.shaman.Shaman
import character.classes.shaman.buffs.WindfuryWeapon
import character.races.Draenei
import data.DB
import data.model.Item
import sim.Sim
import sim.SimOptions
import sim.rotation.Rotation

fun getTestCharacter(): Character {
    val gear = Gear()
    gear.mainHand = Item()
    gear.mainHand.id = 1
    gear.mainHand.temporaryEnhancement = WindfuryWeapon(gear.mainHand)

    gear.offHand = Item()
    gear.offHand.id = 2
    gear.mainHand.temporaryEnhancement = WindfuryWeapon(gear.offHand)

    return Character(
        klass = Shaman(),
        race = Draenei(),
        gear = gear
    )
}

fun getTestRotation(): Rotation {
    return Rotation(
        listOf()
    )
}

fun getSimOpts(): SimOptions {
    return SimOptions(iterations = 1)
}

fun setupLogging() {
    // Debug logging by default
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
}

fun main() {
    setupLogging()
    DB.init()

    Sim(
        getTestCharacter(),
        getTestRotation(),
        getSimOpts()
    ).sim()

//    MainUI()
}
