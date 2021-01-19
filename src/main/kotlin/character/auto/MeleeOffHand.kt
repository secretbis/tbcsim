package character.auto

import sim.Event
import sim.Sim

class MeleeOffHand(sim: Sim) : MeleeBase(sim) {
    override val name: String = "Melee (OH)"
    override fun getEventType(): Event.Type = Event.Type.MELEE_OH

    override fun available(): Boolean {
        return sim.subject.hasOffHandWeapon() && super.available()
    }

    override fun getWeaponMin(): Double {
        return sim.subject.gear.offHand.minDmg.coerceAtLeast(0.0)
    }

    override fun getWeaponMax(): Double {
        return sim.subject.gear.offHand.maxDmg.coerceAtLeast(1.0)
    }

    override fun getWeaponSpeed(): Double {
        return (sim.subject.gear.offHand.speed / (1 + sim.subject.getMeleeHastePct())).coerceAtLeast(0.01)
    }
}
