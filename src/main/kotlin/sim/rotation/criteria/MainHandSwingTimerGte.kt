package sim.rotation.criteria

import character.auto.MeleeBase
import character.auto.MeleeMainHand
import sim.SimIteration
import sim.rotation.Criterion

class MainHandSwingTimerGte(data: Map<String, String?>) : Criterion(Type.MAIN_HAND_SWING_TIMER_GTE, data) {
    val seconds: Double? = try {
        (data["seconds"] as String).toDouble().coerceAtLeast(0.0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'seconds' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'seconds' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sim: SimIteration): Boolean {
        if(seconds == null) return false

        val abilityState = sim.abilityState[MeleeMainHand.name] as MeleeBase.AutoAttackState?
        val speed = sim.weaponSpeed(sim.subject.gear.mainHand)
        return if(abilityState != null) {
            val lastAttackTime = abilityState.lastAttackTimeMs
            val nextAttackTime = lastAttackTime + speed
            return (nextAttackTime - sim.elapsedTimeMs).coerceAtLeast(0.0) >= seconds * 1000.0
        } else false
    }
}
