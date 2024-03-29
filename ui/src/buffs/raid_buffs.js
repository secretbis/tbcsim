import React, { useRef } from 'react';
import _ from 'lodash';

import { Checkbox, Row, Col } from 'rsuite';

import * as tbcsim from 'tbcsim';

const groupStyle = {
  marginBottom: '5px'
};

export default function({ state, stateKey, dispatch }) {
  const form = useRef(null);

  const dataKey = stateKey == 'raidBuffs' ? 'buffNames' : 'debuffNames';
  const allAbilities = tbcsim.data.abilities.raid.RaidAbilities[dataKey];

  function onChange(key, checked) {
    // Dispatch state change
    const action = stateKey == 'raidBuffs' ? 'setRaidBuff' : 'setRaidDebuff';
    dispatch({ type: action, value: {
      name: key,
      value: checked
    }})
  }

  const col1 = Math.ceil(allAbilities.length / 2)
  const col2 = allAbilities.length - col1

  const type = stateKey == 'raidBuffs' ? 'buffs' : 'debuffs';

  return (
    <Row>
      <div style={{ textAlign: 'center' }}><em>Select only {type} provided by *other* members of the raid</em></div>
      <Col xs={12}>
        {_.range(col1).map(key => {
          const name = allAbilities[key]
          return (
            <Checkbox key={key} value={name} checked={state[name]} onChange={onChange}>{name}</Checkbox>
          )
        })}
      </Col>
      <Col xs={12}>
      {_.range(col2).map(key => {
          const name = allAbilities[col1 + key]
          return (
            <Checkbox key={key} value={name} checked={state[name]} onChange={onChange}>{name}</Checkbox>
          )
        })}
      </Col>
    </Row>
  )
}
