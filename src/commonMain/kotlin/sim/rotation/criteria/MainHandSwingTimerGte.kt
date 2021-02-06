package sim.rotation.criteria

import character.auto.MeleeBase
import character.auto.MeleeMainHand
import sim.SimIteration
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class MainHandSwingTimerGte(data: RotationRuleCriterion) : Criterion(Type.MAIN_HAND_SWING_TIMER_GTE, data) {
    class TimerState : State() {
        var lastSwingIndex: Int = -1
    }

    val seconds: Double? = try {
        (data.seconds as Int).toDouble().coerceAtLeast(0.0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'seconds' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'seconds' must be an integer for criterion $type" }
        null
    }

    val oncePerSwing: Boolean = try {
        data.oncePerSwing as Boolean
    } catch (e: NullPointerException) {
        // Default true if missing
        true
    } catch(e: Exception) {
        logger.warn { "Field 'oncePerSwing' must be a boolean value for criterion $type" }
        true
    }

    override fun stateFactory(): State {
        return TimerState()
    }

    override fun satisfied(sim: SimIteration): Boolean {
        if(seconds == null) return false

        // Check our own state, to make sure we don't trigger this twice per mainhand swing
        val timerState = state(sim) as TimerState

        // Check the state of our mainhaind weapon
        val abilityState = sim.abilityState[MeleeMainHand.name] as MeleeBase.AutoAttackState?
        val speed = sim.weaponSpeed(sim.subject.gear.mainHand)

        return if(abilityState != null) {
            val lastAttackTime = abilityState.lastAttackTimeMs
            val nextAttackTime = lastAttackTime + speed
            val remainingSwingTimer =  (nextAttackTime - sim.elapsedTimeMs).coerceAtLeast(0.0)
            val withinBounds = remainingSwingTimer >= seconds * 1000.0

            // Only satisfied if we have not swung yet, or if the swing counter has changed
            if(oncePerSwing) {
                val currentSwingIndex = abilityState.count
                return if (timerState.lastSwingIndex != currentSwingIndex) {
                    // Update timer state with the swing index we just fired on
                    timerState.lastSwingIndex = currentSwingIndex
                    withinBounds
                } else false
            } else withinBounds
        } else false
    }
}
