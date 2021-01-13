package sim

import entity.Entity

class Sim (
    val durationMs: Int = 120000,
    val stepMs: Int = 10,
    val iterations: Int = 1000,
    val target: Entity

) {
    fun sim(subject: Entity) {
        for(iter in 1..iterations) {
            for(step in 0..durationMs step stepMs) {
                subject.step(step)
            }
        }
    }
}