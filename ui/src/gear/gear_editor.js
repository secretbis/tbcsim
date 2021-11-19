import React from 'react'
import { Container, Col, Row } from 'rsuite'

import GearSlot from './gear_slot';
import Stats from './stats';
import { hasAmmo } from '../util/items';

export default function({ state, character, phase, epOptions, dispatch }) {
  character = character || {}

  return (
    <Container style={{ maxWidth: '750px', minWidth: '500px' }}>
      <div style={{ textAlign: 'center', marginBottom: 10, marginTop: -25 }}><em>Click any slot, gem, or enchant to change it!</em></div>
      <Row>
        <Col xs={12}>
          <GearSlot character={character} phase={phase} slotName='head' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='neck' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='shoulders' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='back' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='chest' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='wrists' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='mainHand' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='offHand' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='rangedTotemLibram' epOptions={epOptions} dispatch={dispatch} />
        </Col>
        <Col xs={12}>
          <GearSlot character={character} phase={phase} slotName='hands' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='waist' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='legs' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='feet' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='ring1' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='ring2' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='trinket1' epOptions={epOptions} dispatch={dispatch} />
          <GearSlot character={character} phase={phase} slotName='trinket2' epOptions={epOptions} dispatch={dispatch} />
          {hasAmmo(character.gear.rangedTotemLibram) ? <GearSlot character={character} phase={phase} slotName='ammo' epOptions={epOptions} dispatch={dispatch} /> : null }
        </Col>
      </Row>
      <Row>
        <Stats state={state} />
      </Row>
    </Container>
  );
}
