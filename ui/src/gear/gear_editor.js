import React from 'react'
import { Container, Col, Row } from 'rsuite'

import { itemClasses, armorSubclasses, classArmorSubclasses, classMainHandInvSlots, classOffHandInvSlots, classMainHandItemClasses, classOffHandItemClasses } from '../data/constants';
import GearSlot from './gear_slot';
import Stats from './stats';

export default function({ state, character, dispatch }) {
  character = character || {}

  if(!character.class) {
    return null;
  }

  const armorSlotIC = {
    itemClasses: [itemClasses.armor],
    itemSubclasses: {
      [itemClasses.armor]: classArmorSubclasses[character.class.toLowerCase()]
    }
  };

  const jewelrySlotIC = {
    itemClasses: [itemClasses.armor],
    itemSubclasses: {
      [itemClasses.armor]: [armorSubclasses.misc]
    }
  };

  const mainHandSlotIC = classMainHandItemClasses[character.class.toLowerCase()];
  const offHandSlotIC = classOffHandItemClasses[character.class.toLowerCase()];

  const mainHandInvSlots = classMainHandInvSlots[character.class.toLowerCase()];
  const offHandInvSlots = classOffHandInvSlots[character.class.toLowerCase()];

  return (
    <Container style={{ maxWidth: '750px', minWidth: '500px' }}>
      <Row>
        <Col xs={12}>
          <GearSlot character={character} inventorySlots={[1]} itemClasses={armorSlotIC} slotName='head' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[2]} itemClasses={jewelrySlotIC} slotName='neck' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[3]} itemClasses={armorSlotIC} slotName='shoulders' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[16]} itemClasses={armorSlotIC} slotName='back' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[5]} itemClasses={armorSlotIC} slotName='chest' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[9]} itemClasses={armorSlotIC} slotName='wrists' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={mainHandInvSlots} itemClasses={mainHandSlotIC} slotName='mainHand' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={offHandInvSlots} itemClasses={offHandSlotIC} slotName='offHand' dispatch={dispatch} />
        </Col>
        <Col xs={12}>
          <GearSlot character={character} inventorySlots={[10]} itemClasses={armorSlotIC} slotName='hands' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[6]} itemClasses={armorSlotIC} slotName='waist' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[7]} itemClasses={armorSlotIC} slotName='legs' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[8]} itemClasses={armorSlotIC} slotName='feet' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[11]} itemClasses={jewelrySlotIC} slotName='ring1' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[11]} itemClasses={jewelrySlotIC} slotName='ring2' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[12]} itemClasses={jewelrySlotIC} slotName='trinket1' dispatch={dispatch} />
          <GearSlot character={character} inventorySlots={[12]} itemClasses={jewelrySlotIC} slotName='trinket2' dispatch={dispatch} />
        </Col>
      </Row>
      <Row>
        <Stats state={state} />
      </Row>
    </Container>
  );
}
