package sim.rotation.criteria

import character.auto.AutoAttackBase
import character.auto.AutoShot
import character.auto.MeleeMainHand
import character.auto.MeleeOffHand
import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class SwingTimerRemainingGte(data: RotationRuleCriterion) : Criterion(Type.SWING_TIMER_REMAINING_GTE, data) {
    class TimerState : State() {
        var lastSwingIndex: Int = -1
    }

    val seconds: Double? = try {
        (data.seconds as Double).coerceAtLeast(0.0)
    } catch (e: NullPointerException) {
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

    val swingType: String? = try {
        data.swingType as String
    } catch (e: Exception) {
        logger.warn { "Field 'swingType' is required for criterion $type" }
        null
    }

    val ability: String? = try {
        data.ability as String
    } catch (e: NullPointerException) {
        null
    } catch(e: Exception) {
        logger.warn { "Field 'ability' must be a String value for criterion $type" }
        null
    }

    val maxClipSeconds: Double = try {
        (data.maxClipSeconds as Double).coerceAtLeast(0.0)
    } catch (e: NullPointerException) {
        // Default to zero
        0.0
    } catch(e: Exception) {
        logger.warn { "Field 'maxClipSeconds' must be an integer for criterion $type" }
        0.0
    }

    override fun stateFactory(): State {
        return TimerState()
    }

    override fun satisfied(sp: SimParticipant): Boolean {
        if(seconds == null && ability == null) {
            logger.warn { "Either of fields 'ability' or 'seconds' are required for criterion $type" }
            return false
        }

        val minSwingTimerRemaining = seconds
            ?: if(ability != null) {
                val actualAbility = sp.character.klass.abilityFromString(ability)
                if(actualAbility == null) {
                    return false
                } else {
                    actualAbility.castTimeMs(sp).toDouble() / 1000.0
                }
            } else {
                return false
            }

        // Check our own state, to make sure we don't trigger this twice per mainhand swing
        val timerState = state(sp) as TimerState

        // Check the state of our chosen weapon
        val abilityState = sp.abilityState[swingType] as AutoAttackBase.AutoAttackState?
        val weapon = when(swingType) {
            MeleeMainHand.name -> sp.character.gear.mainHand
            MeleeOffHand.name -> sp.character.gear.offHand
            AutoShot.name -> sp.character.gear.rangedTotemLibram
            else -> return false
        }
        val speed = sp.weaponSpeed(weapon)

        return if(abilityState != null) {
            val lastAttackTime = abilityState.lastAttackTimeMs
            val nextAttackTime = lastAttackTime + speed
            val remainingSwingTimer = (nextAttackTime - sp.sim.elapsedTimeMs).coerceAtLeast(0.0)
            val withinBounds = remainingSwingTimer >= (minSwingTimerRemaining * 1000.0) - (maxClipSeconds * 1000.0)

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
