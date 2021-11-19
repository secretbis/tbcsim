import { inventorySlots as constantInvSlots, itemClasses, armorSubclasses, weaponSubclasses, classArmorSubclasses, classMainHandInvSlots, classOffHandInvSlots, classRangedInvSlots, classMainHandItemClasses, classOffHandItemClasses, classRangedItemClasses, is1HWeapon, gemSubclasses as gsc } from '../data/constants';
import { itemEp } from '../ep/ep_stats';
import { kprop } from '../util/util';

import * as tbcsim from 'tbcsim';
import _ from 'lodash';

export function hasAmmo(item) {
  if(!item) return false

  const itemSubclass = kprop(item.itemSubclass, 'itemClassOrdinal')
  return [weaponSubclasses.bow, weaponSubclasses.crossbow, weaponSubclasses.gun].includes(itemSubclass)
}

export function inventorySlotInfo(character, slotName, itemType) {
  if(!character || !character.class) {
    return null;
  }

  const charClass = character.class.toLowerCase();
  const armorSlotIC = {
    itemClasses: [itemClasses.armor],
    itemSubclasses: {
      [itemClasses.armor]: classArmorSubclasses[charClass]
    }
  };

  const jewelrySlotIC = {
    itemClasses: [itemClasses.armor],
    itemSubclasses: {
      [itemClasses.armor]: [armorSubclasses.misc]
    }
  };

  const mainHandSlotIC = classMainHandItemClasses[charClass];
  const offHandSlotIC = classOffHandItemClasses[charClass];
  const rangedSlotIC = classRangedItemClasses[charClass];

  const mainHandInvSlots = classMainHandInvSlots[charClass];
  const offHandInvSlots = classOffHandInvSlots[charClass];
  const rangedInvSlots = classRangedInvSlots[charClass];

  // Build ammo item classes according to the ranged type, if any
  const projectileISCs = []

  const rangedItem = character.gear.rangedTotemLibram
  if(rangedItem) {
    const rangedItemSubclass = kprop(rangedItem.itemSubclass, 'itemClassOrdinal')
    if([weaponSubclasses.bow, weaponSubclasses.crossbow].includes(rangedItemSubclass)) {
      projectileISCs.push(2)  // Arrow
    }
    if(weaponSubclasses.gun === rangedItemSubclass) {
      projectileISCs.push(3)  // Bullet
    }
  }

  const ammoSlotIC = {
    itemClasses: [itemClasses.projectile],
    itemSubclasses: {
      [itemClasses.projectile]: projectileISCs
    }
  };

  const nonMetaGemIC = {
    itemClasses: [itemClasses.gem],
    itemSubclasses: {
      [itemClasses.gem]: [gsc.red, gsc.blue, gsc.yellow, gsc.purple, gsc.green, gsc.orange]
    }
  };

  const metaGemIC = {
    itemClasses: [itemClasses.gem],
    itemSubclasses: {
      [itemClasses.gem]: [gsc.meta]
    }
  }

  const slotMap = {
    head: {
      slotName: 'head',
      inventorySlots: [1],
      itemClasses: armorSlotIC
    },
    neck: {
      slotName: 'neck',
      inventorySlots: [2],
      itemClasses: jewelrySlotIC
    },
    shoulders: {
      slotName: 'shoulders',
      inventorySlots: [3],
      itemClasses: armorSlotIC
    },
    back: {
      slotName: 'back',
      inventorySlots: [16],
      itemClasses: armorSlotIC
    },
    chest: {
      slotName: 'chest',
      inventorySlots: [5, 20],
      itemClasses: armorSlotIC
    },
    wrists: {
      slotName: 'wrists',
      inventorySlots: [9],
      itemClasses: armorSlotIC
    },
    mainHand: {
      slotName: 'mainHand',
      inventorySlots: mainHandInvSlots,
      itemClasses: mainHandSlotIC
    },
    offHand: {
      slotName: 'offHand',
      inventorySlots: offHandInvSlots,
      itemClasses: offHandSlotIC
    },
    rangedTotemLibram: {
      slotName: 'rangedTotemLibram',
      inventorySlots: rangedInvSlots,
      itemClasses: rangedSlotIC
    },
    hands: {
      slotName: 'hands',
      inventorySlots: [10],
      itemClasses: armorSlotIC
    },
    waist: {
      slotName: 'waist',
      inventorySlots: [6],
      itemClasses: armorSlotIC
    },
    legs: {
      slotName: 'legs',
      inventorySlots: [7],
      itemClasses: armorSlotIC
    },
    feet: {
      slotName: 'feet',
      inventorySlots: [8],
      itemClasses: armorSlotIC
    },
    ring1: {
      slotName: 'ring1',
      inventorySlots: [11],
      itemClasses: jewelrySlotIC
    },
    ring2: {
      slotName: 'ring2',
      inventorySlots: [11],
      itemClasses: jewelrySlotIC
    },
    trinket1: {
      slotName: 'trinket1',
      inventorySlots: [12],
      itemClasses: jewelrySlotIC
    },
    trinket2: {
      slotName: 'trinket2',
      inventorySlots: [12],
      itemClasses: jewelrySlotIC
    },
    ammo: {
      slotName: 'ammo',
      inventorySlots: [24],
      itemClasses: ammoSlotIC
    },
    gem: {
      slotName: 'gem',
      inventorySlots: [0],
      itemClasses: nonMetaGemIC
    },
    metaGem: {
      slotName: 'metaGem',
      inventorySlots: [0],
      itemClasses: metaGemIC
    }
  }

  // Only return itemClasses for regular item slots
  if(!itemType) {
    return slotMap[slotName]
  } else {
    return _.omit(slotMap[slotName], ['itemClasses'])
  }
}

// Some filter functions for itemsForSlot
export function filterByItemName(itemName) {
  return function(item) {
    if(!item) return false;
    return (item.displayName || item.name).toLowerCase().includes(itemName.toLowerCase())
  }
}

export function filter1HOnly() {
  return function(item) {
    if(!item) return false;
    return is1HWeapon(item);
  }
}

// Returns a list of items for a particular slot, given a character context
// Can also return lists of enchants or tempEnchants, if specified as itemType
export function itemsForSlot(slotName, character, phase, itemType, contextItem, filters, epOptions) {
  if(!slotName || !character || !character.class) return null;

  console.log("FULL ITEM FILTER")

  const { inventorySlots, itemClasses } = inventorySlotInfo(character, slotName, itemType)
  let baseData = tbcsim.data.Items
  if(itemType === "enchants") {
    baseData = tbcsim.data.Enchants
  }
  if(itemType === "tempEnchants") {
    baseData = tbcsim.data.TempEnchants
  }

  let items = [];
  for(const inventorySlot of inventorySlots) {
    items = [...items, ...(baseData.bySlot.get_35(inventorySlot) || [])];
  }

  items = items.map(i => i(contextItem))

  // Filter again by equippable item subclasses, if provided
  const filtered = _.filter(
    _.filter(items, item => {
      // Check phase first
      const isInPhase = (item.phase || 1) <= phase
      if(!isInPhase) return false

      if(itemClasses) {
        const itemClass = kprop(item.itemClass, 'ordinal');

        if(itemClasses.itemClasses.includes(itemClass)) {
          const itemSubclass = kprop(item.itemSubclass, 'itemClassOrdinal');
          const subclasses = itemClasses.itemSubclasses[itemClass]

          // Never filter the cloak slot on itemSubclass
          if(item.inventorySlot == constantInvSlots.back) {
            return true
          }

          if(subclasses.includes(itemSubclass)) {
            return filters ? filters.every(f => f(item)) : true
          }
        }

        return false;
      }

      return true;
    }), item => {
      // Filter by allowable classes
      const allowableClasses = [character.class]
      return item.allowableClasses == null || item.allowableClasses.some(it => {
        return allowableClasses.map(it => it.toUpperCase()).includes(kprop(it, 'name', '').toUpperCase());
      })
    });

  // Add item EP if we have a reference point
  if(character) {
    items = items.forEach(i => {
      const ep = itemEp(i, character.epCategory, character.epSpec, epOptions)
      i.ep = ep
    })
  }

  const sortKey = character ? 'ep' : 'itemLevel';
  return _.sortBy(filtered, sortKey).reverse()
}
