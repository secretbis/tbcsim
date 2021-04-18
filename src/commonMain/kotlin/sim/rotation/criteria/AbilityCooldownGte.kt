package sim.rotation.criteria

import data.abilities.generic.GenericAbilities
import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class AbilityCooldownGte(data: RotationRuleCriterion) : Criterion(Type.ABILITY_COOLDOWN_GTE, data) {
    val ability: String? = try {
        data.ability as String
    } catch (e: Exception) {
        logger.warn { "Field 'ability' is required for criterion $type" }
        null
    }

    val seconds: Double? = try {
        (data.seconds as Double).coerceAtLeast(0.0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'seconds' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'seconds' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sp: SimParticipant): Boolean {
        if(ability == null || seconds == null) return false

        val actualAbility = sp.character.klass.abilityFromString(ability) ?: sp.character.race.racialByName(ability) ?: GenericAbilities.byName(ability)
        val abilityCdMs = actualAbility?.currentCooldownMs(sp)
        return abilityCdMs != null && abilityCdMs >= seconds * 1000.0
    }
}
