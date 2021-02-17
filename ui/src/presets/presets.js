import React, { useState } from 'react';
import { Col, Dropdown, Row } from 'rsuite';

import shamanElePreraid from './samples/shaman_ele_preraid.yml'
import shamanEnhSubElePreraid from './samples/shaman_enh_subele_preraid.yml'
import shamanEnhSubRestoPreraid from './samples/shaman_enh_subresto_preraid.yml'
import shamanEnhSubRestoPreraidAnniDw from './samples/shaman_enh_subresto_preraid_annihilator_dw.yml'
import shamanEnhSubRestoPreraidAnniOh from './samples/shaman_enh_subresto_preraid_annihilator_oh.yml'
import warriorArmsPreraid from './samples/warrior_arms_preraid.yml'
import warriorFuryPreraid from './samples/warrior_fury_preraid.yml'

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

export default ({ value, setter }) => {
  const [isOpen, setIsOpen] = useState(false);

  function onSelect(key, evt) {
    const [klass, idx] = key.split('-')
    setter(presets[klass][idx])
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
    <Row>
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
