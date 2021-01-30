# tbcsim

This is a simulation of WoW: TBC DPS - items, abilities, and mechanics.

This information is not very cleanly available, and requires a fair amount of archaeology to discover.
As such, I'm sure there are a thousand subtle bugs.  Please report any if you find them! 

Implemented:
- Classes
  - Enhancement Shaman
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

Not yet implemented:
- All specs that aren't Enhancement Shaman
- Item set bonuses
- Most item enchants
- Racial abilities
- Automated gear comparison

## Example output

```
Completed 1000 iterations in 129 seconds
Buffs
Name                        | AppliedCountAvg | RefreshedCountAvg | UptimePct | AvgDurationSeconds
----------------------------|-----------------|-------------------|-----------|-------------------
Flask of Relentless Assault |            1.00 |              0.00 |   100.00% |             180.00
Flurry                      |           18.79 |             81.03 |    73.30% |               7.02
Grace of Air Totem          |           18.00 |              0.00 |    90.55% |               9.06
Haste (BS Hammer) (MH)      |            4.88 |              2.51 |    32.25% |              11.91
Haste (BS Hammer) (OH)      |            4.92 |              2.40 |    32.28% |              11.81
Haste Potion                |            2.00 |              0.00 |    16.67% |              15.00
Mongoose (MH)               |            3.16 |              1.33 |    30.23% |              17.22
Mongoose (OH)               |            3.11 |              1.25 |    29.48% |              17.07
Rage of the Unraveller      |            3.47 |              1.03 |    21.34% |              11.07
Roasted Clefthoof           |            1.00 |              0.00 |   100.00% |             180.00
Stormstrike (Nature)        |            8.09 |              9.91 |    90.11% |              20.05
Strength of Earth Totem     |            2.00 |              0.00 |    99.77% |              89.79
Thundering Skyfire Diamond  |            4.90 |              2.47 |    32.41% |              11.92
Unleashed Rage              |            1.43 |             98.38 |    97.91% |             122.99
Windfury Totem              |           18.03 |              0.00 |     9.45% |               0.94

Debuffs
Name              | AppliedCountAvg | RefreshedCountAvg | UptimePct | AvgDurationSeconds
------------------|-----------------|-------------------|-----------|-------------------
Flame Shock (DoT) |           14.71 |              0.00 |    94.01% |              11.50

Damage Type Breakdown
Name     | CountAvg | TotalDmgAvg | PctOfTotal
---------|----------|-------------|-----------
PHYSICAL |   252.19 |  200,795.80 |     80.40%
NATURE   |    26.99 |   25,629.52 |     10.26%
FIRE     |    71.60 |   23,309.06 |      9.33%

Ability Breakdown
Name                 | CountAvg | TotalDmgAvg | PctOfTotal | AverageDmg | MedianDmg |   Hit% |  Crit% |  Miss% | Dodge% | Parry% | Glance%
---------------------|----------|-------------|------------|------------|-----------|--------|--------|--------|--------|--------|--------
Melee (MH)           |    96.38 |   76,297.48 |     30.55% |     984.67 |    902.13 | 15.41% | 40.06% | 13.13% |  6.47% |  0.00% |  24.94%
Windfury Weapon (MH) |    12.71 |   40,882.67 |     16.37% |   3,431.77 |  2,568.96 | 55.99% | 37.73% |  0.00% |  6.28% |  0.00% |   0.00%
Melee (OH)           |    95.78 |   37,901.49 |     15.18% |     493.29 |    454.96 | 15.26% | 40.08% | 13.28% |  6.51% |  0.00% |  24.87%
Stormstrike          |    36.00 |   27,371.06 |     10.96% |     813.82 |    755.04 | 55.61% | 37.82% |  0.00% |  6.58% |  0.00% |   0.00%
Earth Shock          |    26.99 |   25,629.52 |     10.26% |   1,102.20 |  1,057.81 | 79.29% |  6.87% | 13.84% |  0.00% |  0.00% |   0.00%
Windfury Weapon (OH) |    11.32 |   18,343.09 |      7.35% |   1,734.57 |  1,292.04 | 54.47% | 38.93% |  0.00% |  6.60% |  0.00% |   0.00%
Flame Shock (DoT)    |    56.89 |   13,573.70 |      5.44% |     277.94 |    263.24 | 79.11% |  6.73% | 14.16% |  0.00% |  0.00% |   0.00%
Flame Shock          |    14.71 |    9,735.36 |      3.90% |     767.83 |    728.47 | 79.14% |  7.04% | 13.82% |  0.00% |  0.00% |   0.00%

************************************************************************************
AVERAGE DPS: 1,387.41
MEDIAN DPS: 1,383.39
STDDEV DPS: 104.89
************************************************************************************

```

## Setup

1. Install Java 14
   - [OpenJDK Downloads](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
1. Install Git
   - [Git Downloads](https://git-scm.com/downloads)
1. `git clone` the project

## Usage

This has only a command-line interface.

```
tbcsim ./samples/enh_shaman_preraid_subresto.yml

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

The first run will take awhile, since compiling the sim plus every item in TBC takes a hot minute.

## Configuration

Tbcsim accepts a yml file as its configuration.  This contains:
- Race, class, level
- Gear, gems, enchants
- Rotation
- Raid buffs and consumables

[Click here to see a full example](./samples/enh_shaman_preraid_subresto.yml)
