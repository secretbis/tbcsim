package character.auto

import sim.Event
import sim.Sim

class MeleeMainHand(sim: Sim) : MeleeBase(sim) {
    override val name: String = "Melee (MH)"

    override fun getEventType(): Event.Type = Event.Type.MELEE_MH

    override fun available(): Boolean {
        return sim.subject.hasMainHandWeapon() && super.available()
    }

    override fun getWeaponMin(): Double {
        return sim.subject.gear.mainHand.minDmg.coerceAtLeast(0.0)
    }

    override fun getWeaponMax(): Double {
        return sim.subject.gear.mainHand.maxDmg.coerceAtLeast(1.0)
    }

    override fun getWeaponSpeed(): Double {
        return (sim.subject.gear.mainHand.speed / (1 + sim.subject.getMeleeHastePct())).coerceAtLeast(0.01)
    }
}
