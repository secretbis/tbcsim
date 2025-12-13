import React, { useState } from 'react';
import _ from 'lodash';
import { Checkbox, Row, Col } from 'rsuite';

import * as tbcsim from 'tbcsim';
import { useDispatchContext, useStateContext } from '../state';

const groupStyle = {
  marginBottom: '5px'
};

export default function({ stateKey }) {
  const state = useStateContext();
  const dispatch = useDispatchContext();

  const dataKey = stateKey == 'raidBuffs' ? 'buffNames' : 'debuffNames';
  const allAbilities = tbcsim.RaidAbilities.getInstance()[dataKey];

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
          const name = allAbilities[key];
          return (
            <div key={key}>
              <Checkbox value={name} checked={state[stateKey][name] ?? false} onChange={onChange}>{name}</Checkbox>
            </div>
          )
        })}
      </Col>
      <Col xs={12}>
      {_.range(col2).map(key => {
          const name = allAbilities[col1 + key]
          return (
            <div key={key}>
              <Checkbox value={name} checked={state[stateKey][name] ?? false} onChange={onChange}>{name}</Checkbox>
            </div>
          )
        })}
      </Col>
    </Row>
  )
}
