import character.Character
import character.Gear
import character.classes.shaman.Shaman
import character.classes.shaman.buffs.WindfuryWeapon
import character.classes.shaman.talents.Flurry
import character.classes.shaman.talents.NaturesGuidance
import character.classes.shaman.talents.WeaponMastery
import character.races.Draenei
import data.DB
import data.model.Item
import kotlinx.coroutines.runBlocking
import sim.Sim
import sim.SimOptions
import sim.rotation.Rotation

fun testCharacter(): Character {
    val gear = Gear()
    gear.mainHand = Item()
    gear.mainHand.id = 1
    gear.mainHand.temporaryEnhancement = WindfuryWeapon(gear.mainHand)

    gear.offHand = Item()
    gear.offHand.id = 2
    gear.offHand.temporaryEnhancement = WindfuryWeapon(gear.offHand)

    return Character(
        klass = Shaman(),
        race = Draenei(),
        gear = gear,
        talents = mapOf(
            Flurry.name to Flurry(5),
            NaturesGuidance.name to NaturesGuidance(3),
            WeaponMastery.name to WeaponMastery(5)
        )
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

fun main() {
    setupLogging()
    DB.init()

    runBlocking {
        Sim(
            testCharacter(),
            testRotation(),
            simOpts()
        ).sim()
    }

//    MainUI()
}
