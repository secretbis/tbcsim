import React from 'react'
import { Container, Col, Row } from 'rsuite'

import { itemClasses, armorSubclasses, weaponSubclasses, classArmorSubclasses, classMainHandInvSlots, classOffHandInvSlots, classRangedInvSlots, classMainHandItemClasses, classOffHandItemClasses, classRangedItemClasses } from '../data/constants';
import GearSlot from './gear_slot';
import Stats from './stats';
import { kprop } from '../util/util';

export default function({ state, character, phase, dispatch }) {
  character = character || {}

  if(!character.class) {
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
  const rangedItem = character.gear.rangedTotemLibram
  const projectileISCs = []

  if(rangedItem) {
    if([weaponSubclasses.bow, weaponSubclasses.crossbow].includes(kprop(rangedItem.itemSubclass, 'itemClassOrdinal'))) {
      projectileISCs.push(2)  // Arrow
    }
    if(weaponSubclasses.gun === kprop(rangedItem.itemSubclass, 'itemClassOrdinal')) {
      projectileISCs.push(3)  // Bullet
    }
  }

  const hasAmmo = !!projectileISCs.length
  const ammoSlotIC = {
    itemClasses: [itemClasses.projectile],
    itemSubclasses: {
      [itemClasses.projectile]: projectileISCs
    }
  };

  return (
    <Container style={{ maxWidth: '750px', minWidth: '500px' }}>
      <div style={{ textAlign: 'center', marginBottom: 10, marginTop: -25 }}><em>Click any slot, gem, or enchant to change it!</em></div>
      <Row>
        <Col xs={12}>
          <GearSlot character={character} phase={phase} inventorySlots={[1]} itemClasses={armorSlotIC} slotName='head' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[2]} itemClasses={jewelrySlotIC} slotName='neck' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[3]} itemClasses={armorSlotIC} slotName='shoulders' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[16]} itemClasses={armorSlotIC} slotName='back' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[5, 20]} itemClasses={armorSlotIC} slotName='chest' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[9]} itemClasses={armorSlotIC} slotName='wrists' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={mainHandInvSlots} itemClasses={mainHandSlotIC} slotName='mainHand' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={offHandInvSlots} itemClasses={offHandSlotIC} slotName='offHand' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={rangedInvSlots} itemClasses={rangedSlotIC} slotName='rangedTotemLibram' dispatch={dispatch} />
        </Col>
        <Col xs={12}>
          <GearSlot character={character} phase={phase} inventorySlots={[10]} itemClasses={armorSlotIC} slotName='hands' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[6]} itemClasses={armorSlotIC} slotName='waist' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[7]} itemClasses={armorSlotIC} slotName='legs' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[8]} itemClasses={armorSlotIC} slotName='feet' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[11]} itemClasses={jewelrySlotIC} slotName='ring1' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[11]} itemClasses={jewelrySlotIC} slotName='ring2' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[12]} itemClasses={jewelrySlotIC} slotName='trinket1' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} inventorySlots={[12]} itemClasses={jewelrySlotIC} slotName='trinket2' dispatch={dispatch} />
          {hasAmmo ? <GearSlot character={character} phase={phase} inventorySlots={[24]} itemClasses={ammoSlotIC} slotName='ammo' dispatch={dispatch} /> : null }
        </Col>
      </Row>
      <Row>
        <Stats state={state} />
      </Row>
    </Container>
  );
}
