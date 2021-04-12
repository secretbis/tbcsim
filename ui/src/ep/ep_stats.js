import _ from 'lodash';
import epData from './data/ep_all.json';

import { isMeleeWeapon, isRangedWeapon } from '../data/constants';

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
  spellCritRating: 'Spell Crit Rating',
  spellHasteRating: 'Spell Haste Rating',
  spellHitRating: 'Spell Hit Rating'
}

export function itemEp(item, category, spec) {
  const data = _.get(epData, `categories[${category}][${spec}]`, {})
  const options = _.get(epData, `options[${spec}]`, {})

  if(!data || !item || !item.stats) {
    return 0
  }

  let itemEp = allStats.reduce((acc, current) => {
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

  // Add in weapon DPS, if applicable
  if((options.benefitsFromMeleeWeaponDps && isMeleeWeapon(item)) || (options.benefitsFromRangedWeaponDps && isRangedWeapon(item))) {
    itemEp += (14 * item.dps) || 0
  }

  return parseFloat((itemEp || 0).toFixed(2))
}
