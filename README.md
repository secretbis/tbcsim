# tbcsim

This is a simulation of WoW: TBC DPS - items, abilities, and mechanics.

Web version here: https://tbcsim.com

This information is not very cleanly available, and requires a fair amount of archaeology to discover.
As such, I'm sure there are a thousand subtle bugs.  Please report any if you find them! 

Implemented:
- Specs
  - Shaman
    - Enhancement
    - Elemental
  - Warlock
    - Affliction
      - UA, Ruin, and DS variants
    - Destruction
      - Fire and Shadow variants
  - Warrior
    - Arms
    - Fury
  - Hunter
    - Beast Mastery
    - Survival
- All items ilvl >= 91 (except some unique on-use/procs)
  - Including gems, socket bonuses, and meta gems
- Full stats breakdowns
  - Every ability with full results
  - Buff casts and uptime
  - DPS, obviously
- Melee and spell mechanics
- Fully customizable rotations
- Raid buffs/debuffs
- Consumables
- Racial abilities
- Item set bonuses, including all tier sets
- Equivalence points for quick gear comparison

Not yet implemented:
- All specs not listed above
- Automated gear comparison

## Calculating EP

To calculate EPs for a single character definition, use the following command:

`./tbcsim --calc-ep-single <path_to_character_definition_file>`

This uses the sim defaults of a step interval of 10ms and an iteration count of 10,000 - both can be adjusted to your preference.  See the CLI usage below, or just run `./tbcsim`.

Higher iteration counts will give more accurate EPs.  Smaller step intervals will provide a higher resolution, which can be useful in some situations like extremely high haste.  Generally speaking, intervals smaller than 10ms are probably not relevant.  

## Example CLI Output

```
Completed 100 iterations in 12 seconds
Resource usage for iteration 71
 100% |***  *  *                                                                                                                                                                           
      |          *** **    *                                                                                                                                                               
      |                    **** *   **                                                                                                                                                     
M     |                              ***  *    *                                       *                                                                                                   
A     |                                         *** *   **                            *** *** *  *                                                                                         
N     |                                                  ***  *    *                  *          *  ***    *                                                                               
A     |                                                             *** * *  *   **  *                      * *** *  *                                                                     
      |                                                                        *                                        ***    *                                                           
      |                                                                                                                         *****    *                                                 
   0% |                                                                                                                                *  * ***    *    *    *    *    *    *    *    *    
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                                                                                         Time (s)                                                                                          
Buffs
Name                        | AppliedCountAvg | RefreshedCountAvg | UptimePct | AvgDurationSeconds
----------------------------|-----------------|-------------------|-----------|-------------------
Flask of Relentless Assault |            1.00 |              0.00 |   100.00% |             180.00
Flurry                      |           16.95 |             92.49 |    78.15% |               8.30
Grace of Air Totem          |           14.54 |              0.00 |    87.21% |              10.80
Haste (BS Hammer) (MH)      |            4.40 |              2.32 |    29.80% |              12.19
Haste (BS Hammer) (OH)      |            4.84 |              2.45 |    32.14% |              11.95
Haste Potion                |            2.00 |              0.00 |    16.67% |              15.00
Mongoose (MH)               |            2.91 |              1.29 |    28.41% |              17.57
Mongoose (OH)               |            2.97 |              1.16 |    27.94% |              16.93
Rage of the Unraveller      |            3.77 |              1.04 |    23.38% |              11.16
Roasted Clefthoof           |            1.00 |              0.00 |   100.00% |             180.00
Shamanistic Focus           |           19.39 |             90.05 |    81.60% |               7.58
Shamanistic Rage            |            1.00 |              0.00 |     8.33% |              15.00
Stormstrike (Nature)        |            2.34 |             13.83 |    92.08% |              70.83
Strength of Earth Totem     |            1.96 |              0.00 |    98.63% |              90.57
Thundering Skyfire Diamond  |            4.87 |              2.17 |    32.04% |              11.84
Thundering Strikes          |            1.00 |              0.00 |   100.00% |             180.00
Unleashed Rage              |            1.31 |            108.13 |    99.15% |             136.24
Water Shield                |            1.00 |              0.00 |   100.00% |             180.00
Windfury Totem              |           14.73 |              0.00 |    12.79% |               1.56

Debuffs
Name              | AppliedCountAvg | RefreshedCountAvg | UptimePct | AvgDurationSeconds
------------------|-----------------|-------------------|-----------|-------------------
Flame Shock (DoT) |           11.40 |              0.00 |    75.76% |              11.96

Damage Type Breakdown
Name     | CountAvg | TotalDmgAvg | PctOfTotal
---------|----------|-------------|-----------
PHYSICAL |   249.57 |  205,864.53 |     87.40%
FIRE     |    56.89 |   19,173.41 |      8.14%
NATURE   |    10.68 |   10,498.67 |      4.46%

Ability Breakdown
Name                 | CountAvg | TotalDmgAvg | PctOfTotal | AverageDmg | MedianDmg |   Hit% |  Crit% |  Miss% | Dodge% | Parry% | Glance%
---------------------|----------|-------------|------------|------------|-----------|--------|--------|--------|--------|--------|--------
Melee (MH)           |    96.99 |   79,976.02 |     33.95% |   1,021.80 |  1,385.11 | 10.96% | 44.32% | 12.52% |  6.78% |  0.00% |  25.41%
Windfury Weapon (MH) |    12.63 |   42,429.76 |     18.01% |   3,595.74 |  2,640.08 | 49.72% | 43.71% |  0.00% |  6.57% |  0.00% |   0.00%
Melee (OH)           |    96.46 |   39,730.95 |     16.87% |     513.52 |    695.06 | 10.71% | 44.36% | 13.60% |  6.19% |  0.00% |  25.14%
Stormstrike          |    32.34 |   25,454.24 |     10.81% |     843.41 |    764.66 | 51.24% | 42.08% |  0.00% |  6.68% |  0.00% |   0.00%
Windfury Weapon (OH) |    11.15 |   18,273.57 |      7.76% |   1,760.46 |  1,305.91 | 52.47% | 40.63% |  0.00% |  6.91% |  0.00% |   0.00%
Flame Shock (DoT)    |    45.49 |   11,249.99 |      4.78% |     288.24 |    263.24 | 72.94% | 12.86% | 14.20% |  0.00% |  0.00% |   0.00%
Earth Shock          |    10.68 |   10,498.67 |      4.46% |   1,146.14 |  1,061.21 | 72.00% | 13.76% | 14.23% |  0.00% |  0.00% |   0.00%
Flame Shock          |    11.40 |    7,923.42 |      3.36% |     801.97 |    728.47 | 71.75% | 14.91% | 13.33% |  0.00% |  0.00% |   0.00%

************************************************************************************
AVERAGE DPS: 1,308.54
MEDIAN DPS: 1,307.88
STDDEV DPS: 130.06
************************************************************************************
```

## Setup

1. Install Java 14
   - [OpenJDK Downloads](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
1. Install Git
   - [Git Downloads](https://git-scm.com/downloads)
1. `git clone` the project

## Usage

This section applies to the command line interface.

```
./tbcsim

------------------------------------------------

Usage: tbcsim [OPTIONS] [CONFIGFILE]

Options:
  --generate                      Autogenerate all item data
  -d, --duration INT              Fight duration in seconds
  -v, --duration-variability INT  Varies the fight duration randomly, plus or
                                  minus zero to this number of seconds
  -s, --step-ms INT               Fight simulation step size, in milliseconds
  -l, --latency INT               Latency to add when casting spells, in
                                  milliseconds
  -i, --iterations INT            Number of simulation iterations to run
  --target-level INT              Target level, from 70 to 73
  -a, --target-armor INT          The target's base armor value, before
                                  debuffs
  -p, --allow-parry-block
  -b, --show-hidden-buffs
  -h, --help                      Show this message and exit

Arguments:
  CONFIGFILE  Path to configuration file

------------------------------------------------

DEFAULTS

Duration:      180s
Duration Var:  0s
Step:          1ms
Latency:       0ms
Iterations:    100
Target Armor:  7700
Target Level:  73
Parry/Block:   false
```

The first run will take awhile, since compiling the sim plus every item, buff, and proc in TBC takes a hot minute.

## Configuration

Tbcsim accepts a yml file as its configuration.  This contains:
- Race, class, level
- Gear, gems, enchants
- Rotation
- Raid buffs and consumables

[Click here to see a full example](ui/src/presets/samples/shaman_enh_subresto_preraid.yml)
