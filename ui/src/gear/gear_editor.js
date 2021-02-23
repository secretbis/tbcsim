import React, { useState } from 'react'
import { Container, Col, Row } from 'rsuite'

import GearSelector from './gear_selector';
import GearSlot from './gear_slot';

export default function({ character }) {
  const [selectorVisible, setSelectorVisible] = useState(false);
  const [selectorSlot, setSelectorSlot] = useState(null);

  function onSelect(item) {
    // TODO: Store and render item
    debugger
  }

  function onClick(slot) {
    setSelectorSlot(slot)
    setSelectorVisible(true)
  }

  character = character || {}
  const gear = character.gear;

  return (
    <Container style={{ maxWidth: '750px', minWidth: '500px' }}>
      <Row>
        <Col xs={12}>
          <GearSlot gear={gear} slot='head' onClick={() => onClick('head')} />
          <GearSlot gear={gear} slot='neck' onClick={() => onClick('neck')} />
          <GearSlot gear={gear} slot='shoulders' onClick={() => onClick('shoulders')} />
          <GearSlot gear={gear} slot='back' onClick={() => onClick('back')} />
          <GearSlot gear={gear} slot='chest' onClick={() => onClick('chest')} />
          <GearSlot gear={gear} slot='wrists' onClick={() => onClick('wrists')} />
          <GearSlot gear={gear} slot='mainHand' onClick={() => onClick('mainHand')} />
          <GearSlot gear={gear} slot='offHand' onClick={() => onClick('offHand')} />
        </Col>
        <Col xs={12}>
          <GearSlot gear={gear} slot='hands' onClick={() => onClick('hands')} />
          <GearSlot gear={gear} slot='waist' onClick={() => onClick('waist')} />
          <GearSlot gear={gear} slot='legs' onClick={() => onClick('legs')} />
          <GearSlot gear={gear} slot='feet' onClick={() => onClick('feet')} />
          <GearSlot gear={gear} slot='ring1' onClick={() => onClick('ring1')} />
          <GearSlot gear={gear} slot='ring2' onClick={() => onClick('ring2')} />
          <GearSlot gear={gear} slot='trinket1' onClick={() => onClick('trinket1')} />
          <GearSlot gear={gear} slot='trinket2' onClick={() => onClick('trinket2')} />
        </Col>
        <GearSelector visible={selectorVisible} setVisible={setSelectorVisible} onSelect={onSelect} />
      </Row>
    </Container>
  );
}
