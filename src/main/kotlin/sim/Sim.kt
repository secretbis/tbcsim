package sim

import character.Character

class Sim (
    val subject: Character,
    val target: Character,
    val opts: SimOptions
) {
    var elapsedTimeMs: Int = 0

    fun sim() {
        // Cast any spells flagged in the rotation as precombat

        // Start combat
        for(iter in 1..opts.iterations) {
            for(step in 0..opts.durationMs step opts.stepMs) {
                elapsedTimeMs = step
                step(subject, step)
            }
        }
    }

    private fun step(subject: Character, time: Int) {

    }
}