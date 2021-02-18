package sim

import de.m3y.kformat.Table
import de.m3y.kformat.table
import sim.statsmodel.*
import java.text.DecimalFormat

object SimStatsPrinter {
    val df = DecimalFormat("#,###.##")

    fun printBuffs(title: String, rows: List<BuffBreakdown>) {
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

    fun printAbilities(rows: List<AbilityBreakdown>) {
        println(
            "Ability Breakdown\n" +
            table {
                header("Name", "CountAvg", "TotalDmgAvg", "PctOfTotal", "AvgHit", "AvgCrit", "Hit%", "Crit%", "Miss%", "Dodge%", "Parry%", "Glance%")

                for(row in rows) {
                    row(row.name, row.countAvg, row.totalAvg, row.pctOfTotal, row.avgHit, row.avgCrit, row.hitPct, row.critPct, row.missPct, row.dodgePct, row.parryPct, row.glancePct)
                }

                hints {
                    alignment(0, Table.Hints.Alignment.LEFT)

                    for(i in 1..11) {
                        precision(i, 2)
                        formatFlag(i, ",")
                    }

                    postfix(3, "%")
                    for(i in 6..11) {
                        postfix(i, "%")
                    }

                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render(StringBuilder())
        )
    }

    fun printDamage(rows: List<DamageTypeBreakdown>) {
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

    fun printDps(dps: DpsBreakdown) {
        println("AVERAGE DPS: ${df.format(dps.mean)}")
        println("MEDIAN DPS: ${df.format(dps.median)}")
        println("STDDEV DPS: ${df.format(dps.sd)}")
    }

    fun printPrecombatStats(sim: SimIteration) {
        println(
            "PLAYER STATS\n" +
            table {
                row("Strength:", sim.strength(), "Phys. Hit:", sim.meleeHitPct())
                row("Agility:", sim.agility(), "Phys. Crit:", sim.meleeCritPct())
                row("Intellect:", sim.intellect(), "Phys. Haste:", 1.0 - sim.meleeHasteMultiplier())
                row("Stamina:", sim.stamina(), "Spell Hit:", sim.spellHitPct())
                row("Spirit:", sim.spirit(), "Spell Crit:", sim.spellCritPct())
                row("Armor Pen:", sim.armorPen(), "Spell Haste:", 1.0 - sim.spellHasteMultiplier())
                row("Attack Power", sim.attackPower(), "Expertise:", sim.expertisePct())
                row("R. Attack Power", sim.rangedAttackPower())
                row("MP5", sim.subjectStats.manaPer5Seconds)
                row("Spell Power", sim.spellDamage())

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
                row("Arcane Res:", sim.targetStats.arcaneResistance, "Armor:", sim.targetArmor())
                row("Fire Res:", sim.targetStats.fireResistance)
                row("Frost Res:", sim.targetStats.frostResistance)
                row("Nature Res:", sim.targetStats.natureResistance)
                row("Shadow Res:", sim.targetStats.shadowResistance)


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
}
