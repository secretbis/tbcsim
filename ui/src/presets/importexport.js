import _ from 'lodash';
import yaml from 'yaml';

export const importPreset = (presetText) => {
  return yaml.parse(presetText);
}

const keyOrder = _.transform([
  // Root keys
  'class',
  'spec',
  'description',
  'race',
  'level',
  'epCategory',
  'epSpec',
  'talents',
  'gear',
  'rotation',
  'pet',
  'raidBuffs',
  'raidDebuffs',

  // Subkeys
  'mainHand',

  // Sub-sub keys
  'name',
  'enchant',
  'gems',
  'rank',
  'criteria',
  'type',
  'precombat',
  'combat'
], (acc, key, idx) => {
  acc[key] = 999 - idx
  return acc;
}, {})

export const exportPreset = (presetObj, raidBuffs, raidDebuffs) => {
  if(!presetObj || !presetObj.gear) {
    throw new Error('Cannot save empty preset');
  }

  // Need to convert the item objects back into JSON
  const cleanGear = _.mapValues(presetObj.gear, item => {
    const name = item.name;
    const gems = item.sockets ? item.sockets.map(socket => socket.gem.name) : []
    const enchant = item.enchant && (item.enchant.displayName || item.enchant.name)
    const tempEnchant = item.tempEnchant && (item.tempEnchant.displayName || item.enchant.name)

    return {
      name,
      gems,
      enchant,
      tempEnchant
    }
  })

  const toSave = {
    ...presetObj,
    gear: cleanGear,
    raidBuffs: _.keys(raidBuffs),
    raidDebuffs: _.keys(raidDebuffs)
  }

  // Sort keys into a useful order
  return yaml.stringify(toSave, {
    sortMapEntries: (a, b) => {
      const predefinedA = keyOrder[a];
      const predefinedB = keyOrder[b];

      // Avoid comparing numbers to letters - sort any predefined value higher than any un-predefined one
      if(predefinedA || predefinedB) {
        if(predefinedA && !predefinedB) {
          return 1;
        }
        if(predefinedB && !predefinedA) {
          return -1;
        }
        return predefinedA > predefinedB ? 1 : -1
      } else {
        // If neither are predefined, compare alphabetically
        return a > b ? 1 : -1
      }
    }
  })
}
