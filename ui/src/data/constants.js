export const inventorySlots = {
  not_equippable: 0,
  head: 1,
  neck: 2,
  shoulders: 3,
  shirt: 4,
  chest: 5,
  waist: 6,
  legs: 7,
  feet: 8,
  wrists: 9,
  hands: 10,
  ring: 11,
  trinket: 12,
  weapon: 13,
  shield: 14,
  ranged: 15,
  back: 16,
  two_hand: 17,
  bag: 18,
  tabard: 19,
  robe: 20,
  main_hand: 21,
  off_hand: 22,
  holdable_tome: 23,
  ammo: 24,
  thrown: 25,
  ranged_right: 26,
  quiver: 27,
  relic: 28
};

export const itemClasses = {
  consumable: 0,
  container: 1,
  weapon: 2,
  gem: 3,
  armor: 4,
  reagent: 5,
  projectile: 6,
  trade_goods: 7,
  generic_obsolete: 8,
  recipe: 9,
  money_obsolete: 10,
  quiver: 11,
  quest: 12,
  key: 13,
  permanent_obsolete: 14,
  miscellaneous: 15,
  glyph: 16,
};

export const gemSubclasses = {
  red: 0,
  blue: 1,
  yellow: 2,
  purple: 3,
  green: 4,
  orange: 5,
  meta: 6,
  simple: 7,
  prismatic: 8,
};

export const weaponSubclasses = {
  axe_1h: 0,
  axe_2h: 1,
  bow: 2,
  gun: 3,
  mace_1h: 4,
  mace_2h: 5,
  polearm: 6,
  sword_1h: 7,
  sword_2h: 8,
  obsolete: 9,
  staff: 10,
  exotic_1: 11,
  exotic_2: 12,
  fist: 13,
  misc_tool: 14,
  dagger: 15,
  thrown: 16,
  spear: 17,
  crossbow: 18,
  wand: 19,
  fishing_pole: 20,
};

export const armorSubclasses = {
  misc: 0,
  cloth: 1,
  leather: 2,
  mail: 3,
  plate: 4,
  buckler_obsolete: 5,
  shield: 6,
  libram: 7,
  idol: 8,
  totem: 9,
}

export const allowableClasses = {
  warrior: 0,
  paladin: 1,
  hunter: 2,
  rogue: 3,
  priest: 4,
  deathknight: 5,
  shaman: 6,
  mage: 7,
  warlock: 8,
  druid: 9,
};

// Technically some of these can wear cloth, but literally will never do so (e.g. hunter, ret paladin)
const asc = armorSubclasses
const wsc = weaponSubclasses
const ic = itemClasses
const inv = inventorySlots

export const classArmorSubclasses = {
  druid: [asc.cloth, asc.leather],
  hunter: [asc.leather, asc.mail],
  mage: [asc.cloth],
  paladin: [asc.leather, asc.mail, asc.plate],
  priest: [asc.cloth],
  rogue: [asc.leather],
  shaman: [asc.cloth, asc.leather, asc.mail],
  warlock: [asc.cloth],
  warrior: [asc.leather, asc.mail, asc.plate]
};

export const classMainHandInvSlots = {
  druid: [inv.weapon, inv.two_hand, inv.main_hand],
  hunter: [inv.weapon, inv.two_hand, inv.main_hand],
  mage: [inv.weapon, inv.two_hand, inv.main_hand],
  paladin: [inv.weapon, inv.two_hand, inv.main_hand],
  priest: [inv.weapon, inv.two_hand, inv.main_hand],
  rogue: [inv.weapon, inv.main_hand],
  shaman: [inv.weapon, inv.two_hand, inv.main_hand],
  warlock: [inv.weapon, inv.two_hand, inv.main_hand],
  warrior: [inv.weapon, inv.two_hand, inv.main_hand]
};

export const classOffHandInvSlots = {
  druid: [inv.holdable_tome],
  hunter: [inv.weapon, inv.off_hand, inv.holdable_tome],
  mage: [inv.holdable_tome],
  paladin: [inv.shield, inv.holdable_tome],
  priest: [inv.holdable_tome],
  rogue: [inv.weapon, inv.off_hand, inv.holdable_tome],
  shaman: [inv.weapon, inv.shield, inv.off_hand, inv.holdable_tome],
  warlock: [inv.holdable_tome],
  warrior: [inv.weapon, inv.shield, inv.off_hand, inv.holdable_tome]
};

export const classRangedInvSlots = {
  druid: [inv.relic],
  hunter: [inv.ranged, inv.ranged_right],
  mage: [inv.ranged_right],
  paladin: [inv.relic],
  priest: [inv.ranged_right],
  rogue: [inv.ranged, inv.ranged_right, inv.thrown],
  shaman: [inv.relic],
  warlock: [inv.ranged_right],
  warrior: [inv.ranged, inv.ranged_right, inv.thrown]
};

export const classMainHandItemClasses = {
  druid: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.dagger, wsc.fist, wsc.mace_1h, wsc.mace_2h, wsc.staff]
    }
  },
  hunter: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.axe_1h, wsc.axe_2h, wsc.dagger, wsc.fist, wsc.polearm, wsc.staff, wsc.sword_1h, wsc.sword_2h]
    }
  },
  mage: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.dagger, wsc.staff, wsc.sword_1h]
    }
  },
  paladin: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.axe_1h, wsc.axe_2h, wsc.mace_1h, wsc.mace_2h, wsc.polearm, wsc.sword_1h, wsc.sword_2h]
    }
  },
  priest: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.dagger, wsc.mace_1h, wsc.staff]
    }
  },
  rogue: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.dagger, wsc.fist, wsc.mace_1h, wsc.sword_1h]
    }
  },
  shaman: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.axe_1h, wsc.axe_2h, wsc.dagger, wsc.fist, wsc.mace_1h, wsc.mace_2h, wsc.staff]
    }
  },
  warlock: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.dagger, wsc.staff, wsc.sword_1h]
    }
  },
  warrior: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.axe_1h, wsc.axe_2h, wsc.dagger, wsc.fist, wsc.mace_1h, wsc.mace_2h, wsc.polearm, wsc.staff, wsc.sword_1h, wsc.sword_2h]
    }
  }
};

export const classOffHandItemClasses = {
  druid: {
    itemClasses: [ic.armor],
    itemSubclasses: {
      [ic.armor]: [asc.misc]
    }
  },
  hunter: {
    itemClasses: [ic.weapon, ic.armor],
    itemSubclasses: {
      [ic.weapon]: [wsc.axe_1h, wsc.dagger, wsc.fist, wsc.sword_1h],
      [ic.armor]: [asc.misc]
    }
  },
  mage: {
    itemClasses: [ic.armor],
    itemSubclasses: {
      [ic.armor]: [asc.misc]
    }
  },
  paladin: {
    itemClasses: [ic.armor],
    itemSubclasses: {
      [ic.armor]: [asc.misc, asc.shield]
    }
  },
  priest: {
    itemClasses: [ic.armor],
    itemSubclasses: {
      [ic.armor]: [asc.misc]
    }
  },
  rogue: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.dagger, wsc.fist, wsc.mace_1h, wsc.sword_1h]
    }
  },
  shaman: {
    itemClasses: [ic.weapon, ic.armor],
    itemSubclasses: {
      [ic.weapon]: [wsc.axe_1h, wsc.dagger, wsc.fist, wsc.mace_1h],
      [ic.armor]: [asc.misc]
    }
  },
  warlock: {
    itemClasses: [ic.armor],
    itemSubclasses: {
      [ic.armor]: [asc.misc]
    }
  },
  warrior: {
    itemClasses: [ic.weapon, ic.armor],
    itemSubclasses: {
      [ic.weapon]: [wsc.axe_1h, wsc.dagger, wsc.fist, wsc.mace_1h, wsc.sword_1h],
      [ic.armor]: [asc.misc, asc.shield]
    }
  }
};

export const classRangedItemClasses = {
  druid: {
    itemClasses: [ic.armor],
    itemSubclasses: {
      [ic.armor]: [asc.idol]
    }
  },
  hunter: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.bow, wsc.crossbow, wsc.gun]
    }
  },
  mage: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [asc.wand]
    }
  },
  paladin: {
    itemClasses: [ic.armor],
    itemSubclasses: {
      [ic.armor]: [asc.libram]
    }
  },
  priest: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [asc.wand]
    }
  },
  rogue: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.bow, wsc.crossbow, wsc.gun, wsc.thrown]
    }
  },
  shaman: {
    itemClasses: [ic.armor],
    itemSubclasses: {
      [ic.armor]: [asc.totem]
    }
  },
  warlock: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [asc.wand]
    }
  },
  warrior: {
    itemClasses: [ic.weapon],
    itemSubclasses: {
      [ic.weapon]: [wsc.bow, wsc.crossbow, wsc.gun, wsc.thrown]
    }
  }
};

export function isMeleeWeapon(item) {
  if(!item) return false

  const validSubclasses = [wsc.axe_1h, wsc.axe_2h, wsc.dagger, wsc.fist, wsc.mace_1h, wsc.mace_2h, wsc.polearm, wsc.staff, wsc.sword_1h, wsc.sword_2h]
  return item.itemClass._ordinal === itemClasses.weapon &&
         validSubclasses.includes(item.itemSubclass._itemClassOrdinal)
}

export function isRangedWeapon(item) {
  if(!item) return false

  const validSubclasses = [wsc.bow, wsc.crossbow, wsc.gun, wsc.thrown]
  return item.itemClass._ordinal === itemClasses.weapon &&
         validSubclasses.includes(item.itemSubclass._itemClassOrdinal)
}

export const classes = {
  druid: {
    name: 'Druid',
    icon: 'classes/druid.png'
  },
  hunter: {
    name: 'Hunter',
    icon: 'classes/hunter.png'
  },
  mage: {
    name: 'Mage',
    icon: 'classes/mage.png'
  },
  paladin: {
    name: 'Paladin',
    icon: 'classes/paladin.png'
  },
  priest: {
    name: 'Priest',
    icon: 'classes/priest.png'
  },
  rogue: {
    name: 'Rogue',
    icon: 'classes/rogue.png'
  },
  shaman: {
    name: 'Shaman',
    icon: 'classes/shaman.png'
  },
  warlock: {
    name: 'Warlock',
    icon: 'classes/warlock.png'
  },
  warrior: {
    name: 'Warrior',
    icon: 'classes/warrior.png'
  },
}
