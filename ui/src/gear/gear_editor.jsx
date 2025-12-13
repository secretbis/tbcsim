import React from 'react'
import { Container, Col, Row } from 'rsuite'

import GearSlot from './gear_slot';
import Stats from './stats';
import { hasAmmo } from '../util/items';
import { useStateContext } from '../state';

export default function() {
  const { character = {} } = useStateContext();

  return (
    <Container style={{ maxWidth: '750px', minWidth: '500px' }}>
      <div style={{ textAlign: 'center', marginBottom: 10, marginTop: -25 }}><em>Click any slot, gem, or enchant to change it!</em></div>
      <Row>
        <Col xs={12}>
          <GearSlot slotName='head' />
          <GearSlot slotName='neck' />
          <GearSlot slotName='shoulders' />
          <GearSlot slotName='back' />
          <GearSlot slotName='chest' />
          <GearSlot slotName='wrists' />
          <GearSlot slotName='mainHand' />
          <GearSlot slotName='offHand' />
          <GearSlot slotName='rangedTotemLibram' />
        </Col>
        <Col xs={12}>
          <GearSlot slotName='hands' />
          <GearSlot slotName='waist' />
          <GearSlot slotName='legs' />
          <GearSlot slotName='feet' />
          <GearSlot slotName='ring1' />
          <GearSlot slotName='ring2' />
          <GearSlot slotName='trinket1' />
          <GearSlot slotName='trinket2' />
          {hasAmmo(character.gear.rangedTotemLibram) ? <GearSlot slotName='ammo' /> : null }
        </Col>
      </Row>
      <Row style={{ marginTop: '20px' }}>
        <Col xs={24}>
          <Stats />
        </Col>
      </Row>
    </Container>
  );
}
