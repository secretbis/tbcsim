import epData from './data/ep_all.json';

export const allStats = [
  'attackPower',
  'rangedAttackPower',
  'agility',
  'intellect',
  'strength',
  'physicalCritRating',
  'physicalHasteRating',
  'physicalHitRating',
  'armorPen',
  'expertiseRating',
  'spellDamage',
  'spellCritRating',
  'spellHasteRating',
  'spellHitRating'
]

export const statDisplayNames = {
  attackPower: 'Attack Power',
  rangedAttackPower: 'Ranged Attack Power',
  agility: 'Agility',
  intellect: 'Intellect',
  strength: 'Strength',
  physicalCritRating: 'Crit Rating',
  physicalHasteRating: 'Haste Rating',
  physicalHitRating: 'Hit Rating',
  armorPen: 'Armor Pen',
  expertiseRating: 'Expertise Rating',
  spellDamage: 'Spell Damage',
  spellCritRating: 'Crit Rating',
  spellHasteRating: 'Haste Rating',
  spellHitRating: 'Hit Rating'
}

export function itemEp(item, category, spec) {
  const data = (epData[category] || {})[spec]

  if(!data || !item || !item.stats) {
    return 0
  }

  const itemEp = allStats.reduce((acc, current) => {
    const currentStatEp = data[current]
    if(currentStatEp) {
      const baseStatsEp = (item.stats['_' + current] || 0) * currentStatEp
      const buffsEp = item.buffs && item.buffs._array_3 && item.buffs._array_3.reduce((acc2, buff) => {
        const permanentStats = buff.permanentStats_18 && buff.permanentStats_18()
        const permanentStatsEp = permanentStats ? (permanentStats['_' + current] || 0) * currentStatEp : 0

        // TODO: Have a static EP estimate for other item procs, and etc.
        // TODO: Socket EP

        return acc2 + permanentStatsEp
      }, 0) || 0
      return acc + baseStatsEp + buffsEp
    }

    return acc
  }, 0)

  return parseFloat((itemEp || 0).toFixed(2))
}
