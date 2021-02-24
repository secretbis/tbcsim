import React, { useState } from 'react';
import { Col, Dropdown, Row } from 'rsuite';
import _ from 'lodash';

import shamanElePreraid from './samples/shaman_ele_preraid.yml'
import shamanEnhSubElePreraid from './samples/shaman_enh_subele_preraid.yml'
import shamanEnhSubRestoPreraid from './samples/shaman_enh_subresto_preraid.yml'
import shamanEnhSubRestoPreraidAnniDw from './samples/shaman_enh_subresto_preraid_annihilator_dw.yml'
import shamanEnhSubRestoPreraidAnniOh from './samples/shaman_enh_subresto_preraid_annihilator_oh.yml'
import warriorArmsPreraid from './samples/warrior_arms_preraid.yml'
import warriorFuryPreraid from './samples/warrior_fury_preraid.yml'

import * as tbcsim from 'tbcsim';

const presets = {
  shaman: [
    shamanElePreraid,
    shamanEnhSubElePreraid,
    shamanEnhSubRestoPreraid,
    shamanEnhSubRestoPreraidAnniDw,
    shamanEnhSubRestoPreraidAnniOh,
  ],
  warrior: [
    warriorArmsPreraid,
    warriorFuryPreraid,
  ]
}

export default ({ value, dispatch }) => {
  const [isOpen, setIsOpen] = useState(false);

  function onSelect(key, evt) {
    const [klass, idx] = key.split('-')
    const clone = JSON.parse(JSON.stringify(presets[klass][idx]))
    clone.gear = _.mapValues(clone.gear, rawItem => {
      // TODO: This method is code generator internals, and possibly fragile
      const item = tbcsim.data.Items.byName.get_35(rawItem.name)
      if(rawItem.gems) {
        rawItem.gems.forEach((gemName, idx) => {
          const gem = tbcsim.data.Items.byName.get_35(gemName)
          item.sockets[idx].gem = gem
        })
      }

      if(rawItem.enchant) {
        debugger
      }

      return item
    })
    dispatch({ type: 'loadCharacterPreset', value: clone })
    setIsOpen(false)
  }

  function presetsFor(klass) {
    return <>
      {presets[klass].map((p, idx) => {
        const key = `${klass}-${idx}`;
        return <Dropdown.Item key={key} eventKey={key} onSelect={onSelect}>{p.description}</Dropdown.Item>
      })}
    </>
  }

  return (
    <Row style={{padding: '10px 0px', fontWeight: 800}}>
      <Col>
        <Dropdown title="Presets"
          onMouseEnter={() => setIsOpen(true)}
          onMouseLeave={() => setIsOpen(false)}
          open={isOpen}
        >
          <Dropdown.Menu title="Shaman">
            {presetsFor("shaman")}
          </Dropdown.Menu>
          <Dropdown.Menu title="Warrior">
            {presetsFor("warrior")}
          </Dropdown.Menu>
        </Dropdown>

        {value && value.description ?
          <span>Selected Preset: {value.description}</span> :
          <span>Please select a preset</span>
        }
      </Col>
    </Row>
  )
}
