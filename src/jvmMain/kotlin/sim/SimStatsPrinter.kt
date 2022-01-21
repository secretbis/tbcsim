package sim

import de.m3y.kformat.Table
import de.m3y.kformat.table
import sim.rotation.Rotation
import sim.statsmodel.*
import java.text.DecimalFormat

object SimStatsPrinter {
    val df = DecimalFormat("#,###.##")

    fun precombatStats(iteration: SimIteration) {
        printPrecombatStats(iteration)

        println("ACTIVE RAID BUFFS")
        iteration.subject.rotation.rules.filter { it.phase == Rotation.Phase.RAID_OR_PARTY }.forEach {
            println(" - ${it.ability.name}")
        }
        println()
    }

    fun printBuffs(title: String, participants: List<List<BuffBreakdown>>) {
        participants.forEach { rows ->
            println(
                "$title\n" +
                table {
                    header("Name", "AppliedCountAvg", "RefreshedCountAvg", "UptimePct", "AvgDurationSeconds", "AvgStacks")

                    for(row in rows) {
                        row(row.name, row.appliedAvg, row.refreshedAvg, row.uptimePct, row.avgDuration, row.avgStacks)
                    }

                    hints {
                        alignment(0, Table.Hints.Alignment.LEFT)

                        for(i in 1..5) {
                            precision(i, 2)
                            formatFlag(i, ",")
                        }

                        for(i in 3..3) {
                            postfix(i, "%")
                        }

                        borderStyle = Table.BorderStyle.SINGLE_LINE
                    }
                }.render(StringBuilder())
            )
        }
    }

    fun printAbilities(participants: List<List<AbilityBreakdown>>) {
        participants.forEach { rows ->
            println(
                "Ability Breakdown\n" +
                table {
                    header("Name", "CountAvg", "TotalDmgAvg", "PctOfTotal", "MinHit", "AvgHit", "MaxHit", "MinCrit", "AvgCrit", "MaxCrit", "Hit%", "Crit%", "Miss%", "Dodge%", "Parry%", "Glance%")

                    for(row in rows) {
                        row(row.name, row.countAvg, row.totalAvg, row.pctOfTotal, row.minHit, row.avgHit, row.maxHit, row.minCrit, row.avgCrit, row.maxCrit, row.hitPct, row.critPct, row.missPct, row.dodgePct, row.parryPct, row.glancePct)
                    }

                    hints {
                        alignment(0, Table.Hints.Alignment.LEFT)

                        for(i in 1..15) {
                            precision(i, 2)
                            formatFlag(i, ",")
                        }

                        postfix(3, "%")
                        for(i in 10..15) {
                            postfix(i, "%")
                        }

                        borderStyle = Table.BorderStyle.SINGLE_LINE
                    }
                }.render(StringBuilder())
            )
        }
    }

    fun printDamage(participants: List<List<DamageTypeBreakdown>>) {
        participants.forEach { rows ->
            println(
                "Damage Type Breakdown\n" +
                table {
                    header("Name", "CountAvg", "TotalDmgAvg", "PctOfTotal")

                    for(row in rows) {
                        row(row.type, row.countAvg, row.totalAvg, row.pctOfTotal)
                    }

                    hints {
                        alignment(0, Table.Hints.Alignment.LEFT)

                        for(i in 1..3) {
                            precision(i, 2)
                        }

                        for(i in 1..11) {
                            formatFlag(i, ",")
                        }

                        postfix(3, "%")

                        borderStyle = Table.BorderStyle.SINGLE_LINE
                    }
                }.render(StringBuilder())
            )
        }
    }

    // The player's pet is considered "their" DPS, so merge them if present
    fun printDps(subjectDps: DpsBreakdown, subjectPetDps: DpsBreakdown?) {
        val petMeanPct =  (subjectPetDps?.mean ?: 0.0) / (subjectDps.mean + (subjectPetDps?.mean ?: 0.0)) * 100.0
        val petMedianPct = (subjectPetDps?.mean ?: 0.0) / (subjectDps.median + (subjectPetDps?.median ?: 0.0)) * 100.0

        println(
            "AVERAGE DPS: ${df.format(subjectDps.mean)}" +
            if(subjectPetDps != null) { " - PET: ${df.format(subjectPetDps.mean)} (${df.format(petMeanPct)}%) - TOTAL: ${df.format(subjectDps.mean + subjectPetDps.mean)}" } else ""
        )
        println(
            "MEDIAN DPS: ${df.format(subjectDps.median)}" +
            if(subjectPetDps != null) { " - PET: ${df.format(subjectPetDps.median)} (${df.format(petMedianPct)}%) - TOTAL: ${df.format(subjectDps.median + subjectPetDps.median)}" } else ""
        )
        println(
            "STDDEV DPS: ${df.format(subjectDps.sd)}" +
            if(subjectPetDps != null) { " - PET: ${df.format(subjectPetDps.sd)}" } else ""
        )
    }

    fun printPrecombatStats(iteration: SimIteration) {
        println(
            "PLAYER STATS\n" +
            table {
                row("Strength:", iteration.subject.strength(), "Phys. Hit:", iteration.subject.physicalHitPct())
                row("Agility:", iteration.subject.agility(), "Melee Crit:", iteration.subject.meleeCritPct())
                row("Intellect:", iteration.subject.intellect(), "Ranged Crit:", iteration.subject.rangedCritPct())
                row("Stamina:", iteration.subject.stamina(), "Phys. Haste:", iteration.subject.physicalHasteMultiplier() - 1.0)
                row("Spirit:", iteration.subject.spirit(), "Spell Hit:", iteration.subject.spellHitPct())
                row("Armor Pen:", iteration.subject.armorPen(), "Spell Crit:", iteration.subject.spellCritPct())
                row("Attack Power", iteration.subject.attackPower(), "Spell Haste:", iteration.subject.spellHasteMultiplier() - 1.0)
                row("R. Attack Power", iteration.subject.rangedAttackPower(), "Expertise:", iteration.subject.expertisePct())
                row("MP5", iteration.subject.stats.manaPer5Seconds)
                row("Spell Power", iteration.subject.spellDamage())

                hints {
                    alignment(0, Table.Hints.Alignment.RIGHT)
                    alignment(1, Table.Hints.Alignment.LEFT)
                    alignment(2, Table.Hints.Alignment.RIGHT)
                    alignment(3, Table.Hints.Alignment.LEFT)
                    alignment(4, Table.Hints.Alignment.RIGHT)
                    alignment(5, Table.Hints.Alignment.LEFT)

                    precision(1, 0)
                    precision(3, 2)
                    precision(5, 2)

                    postfix(3, "%")

                    borderStyle = Table.BorderStyle.NONE
                }
            }.render(StringBuilder())
        )

        println(
            "TARGET STATS\n" +
            table {
                row("Arcane Res:", iteration.target.stats.arcaneResistance, "Armor:", iteration.target.armor())
                row("Fire Res:", iteration.target.stats.fireResistance)
                row("Frost Res:", iteration.target.stats.frostResistance)
                row("Nature Res:", iteration.target.stats.natureResistance)
                row("Shadow Res:", iteration.target.stats.shadowResistance)

                hints {
                    alignment(0, Table.Hints.Alignment.RIGHT)
                    alignment(1, Table.Hints.Alignment.LEFT)
                    alignment(2, Table.Hints.Alignment.RIGHT)
                    alignment(3, Table.Hints.Alignment.LEFT)
                    alignment(4, Table.Hints.Alignment.RIGHT)
                    alignment(5, Table.Hints.Alignment.LEFT)

                    precision(1, 0)

                    borderStyle = Table.BorderStyle.NONE
                }
            }.render(StringBuilder())
        )
    }

    fun printResourceUsageByAbility(participants: List<Map<String, List<ResourceByAbility>>>) {
        participants.forEach { resourceType ->
            resourceType.entries.forEach { resourceData ->
                val rows = resourceData.value
                println(
                    "Resource by Ability Breakdown (${resourceData.key})\n" +
                    table {
                        header("Name", "CountAvg", "TotalGainAvg", "GainPerCountAvg")

                        for(row in rows) {
                            row(row.name, row.countAvg, row.totalGainAvg, row.gainPerCountAvg)
                        }

                        hints {
                            alignment(0, Table.Hints.Alignment.LEFT)

                            for(i in 1..3) {
                                precision(i, 2)
                                formatFlag(i, ",")
                            }

                            borderStyle = Table.BorderStyle.SINGLE_LINE
                        }
                    }.render(StringBuilder())
                )
            }
        }
    }

    fun printThreat(participants: List<List<ThreatByAbility>>) {
        participants.forEach { rows ->
            println(
                "Threat by Ability Breakdown\n" +
                table {
                    header("Name", "CountAvg", "ThreatPerSecAvg", "ThreatPerCastAvg")

                    for(row in rows) {
                        row(row.name, row.countAvg, row.threatPerSecondAvg, row.threatPerCastAvg)
                    }

                    hints {
                        alignment(0, Table.Hints.Alignment.LEFT)

                        for(i in 1..3) {
                            precision(i, 2)
                            formatFlag(i, ",")
                        }

                        borderStyle = Table.BorderStyle.SINGLE_LINE
                    }
                }.render(StringBuilder())
            )
            println("Total TPS: " + df.format(rows.sumOf { it.threatPerSecondAvg }))
            println()
        }
    }
}
