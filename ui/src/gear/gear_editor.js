import React from 'react'
import { Container, Col, Row } from 'rsuite'

import GearSlot from './gear_slot';
import Stats from './stats';
import { hasAmmo } from '../util/items';

export default function({ state, character, phase, dispatch }) {
  character = character || {}

  return (
    <Container style={{ maxWidth: '750px', minWidth: '500px' }}>
      <div style={{ textAlign: 'center', marginBottom: 10, marginTop: -25 }}><em>Click any slot, gem, or enchant to change it!</em></div>
      <Row>
        <Col xs={12}>
          <GearSlot character={character} phase={phase} slotName='head' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='neck' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='shoulders' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='back' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='chest' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='wrists' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='mainHand' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='offHand' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='rangedTotemLibram' dispatch={dispatch} />
        </Col>
        <Col xs={12}>
          <GearSlot character={character} phase={phase} slotName='hands' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='waist' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='legs' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='feet' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='ring1' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='ring2' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='trinket1' dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='trinket2' dispatch={dispatch} />
          {hasAmmo(character.gear.rangedTotemLibram) ? <GearSlot character={character} phase={phase} slotName='ammo' dispatch={dispatch} /> : null }
        </Col>
      </Row>
      <Row>
        <Stats state={state} />
      </Row>
    </Container>
  );
}
