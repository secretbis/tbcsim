package sim

import de.m3y.kformat.Table
import de.m3y.kformat.table
import sim.rotation.Rotation
import sim.statsmodel.*
import java.text.DecimalFormat

object SimStatsPrinter {
    val df = DecimalFormat("#,###.##")

    fun precombatStats(sp: SimParticipant) {
        printPrecombatStats(sp)

        println("ACTIVE RAID BUFFS")
        sp.rotation.rules.filter { it.phase == Rotation.Phase.RAID_OR_PARTY }.forEach {
            println(" - ${it.ability.name}")
        }
        println()
    }

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

    fun printPrecombatStats(sp: SimParticipant) {
        println(
            "PLAYER STATS\n" +
            table {
                row("Strength:", sp.strength(), "Phys. Hit:", sp.meleeHitPct())
                row("Agility:", sp.agility(), "Phys. Crit:", sp.meleeCritPct())
                row("Intellect:", sp.intellect(), "Phys. Haste:", sp.meleeHasteMultiplier() - 1.0)
                row("Stamina:", sp.stamina(), "Spell Hit:", sp.spellHitPct())
                row("Spirit:", sp.spirit(), "Spell Crit:", sp.spellCritPct())
                row("Armor Pen:", sp.armorPen(), "Spell Haste:", sp.spellHasteMultiplier() - 1.0)
                row("Attack Power", sp.attackPower(), "Expertise:", sp.expertisePct())
                row("R. Attack Power", sp.rangedAttackPower())
                row("MP5", sp.stats.manaPer5Seconds)
                row("Spell Power", sp.spellDamage())

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
                row("Arcane Res:", sp.sim.target.stats.arcaneResistance, "Armor:", sp.sim.target.armor())
                row("Fire Res:", sp.sim.target.stats.fireResistance)
                row("Frost Res:", sp.sim.target.stats.frostResistance)
                row("Nature Res:", sp.sim.target.stats.natureResistance)
                row("Shadow Res:", sp.sim.target.stats.shadowResistance)


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
