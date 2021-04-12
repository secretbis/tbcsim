import React, { useState } from 'react';
import { Col, Dropdown, Row } from 'rsuite';
import _ from 'lodash';

import hunterBmPreraid from './samples/hunter_bm_preraid.yml'
import hunterSurvPreraid from './samples/hunter_surv_preraid.yml'
import shamanElePreraid from './samples/shaman_ele_preraid.yml'
import shamanEnhSubElePreraid from './samples/shaman_enh_subele_preraid.yml'
import shamanEnhSubRestoPreraid from './samples/shaman_enh_subresto_preraid.yml'
import shamanEnhSubRestoPreraidAnniDw from './samples/shaman_enh_subresto_preraid_annihilator_dw.yml'
import shamanEnhSubRestoPreraidAnniOh from './samples/shaman_enh_subresto_preraid_annihilator_oh.yml'
import warlockAfflictionUAPreraid from './samples/warlock_affliction_ua_preraid.yml'
import warlockAfflictionRuinPreraid from './samples/warlock_affliction_ruin_preraid.yml'
import warlockAfflictionDSPreraid from './samples/warlock_affliction_ds_preraid.yml'
import warlockDestructionFirePreraid from './samples/warlock_destruction_fire_preraid.yml'
import warlockDestructionShadowPreraid from './samples/warlock_destruction_shadow_preraid.yml'
import warriorArmsPreraid from './samples/warrior_arms_preraid.yml'
import warriorFuryPreraid from './samples/warrior_fury_preraid.yml'

import * as tbcsim from 'tbcsim';

const presets = {
  hunter: [
    hunterBmPreraid,
    hunterSurvPreraid
  ],
  shaman: [
    shamanElePreraid,
    shamanEnhSubElePreraid,
    shamanEnhSubRestoPreraid,
    shamanEnhSubRestoPreraidAnniDw,
    shamanEnhSubRestoPreraidAnniOh,
  ],
  warlock: [
    warlockDestructionFirePreraid,
    warlockDestructionShadowPreraid,
    warlockAfflictionUAPreraid,
    warlockAfflictionRuinPreraid,
    warlockAfflictionDSPreraid
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
      let item = tbcsim.data.Items.byName.get_35(rawItem.name)
      if(!item) {
         return;
      }
      item = item()

      if(rawItem.gems) {
        rawItem.gems.forEach((gemName, idx) => {
          const gem = tbcsim.data.Items.byName.get_35(gemName)()
          item.sockets[idx].gem = gem
        })
      }

      if(rawItem.enchant) {
        const enchant = tbcsim.data.Enchants.byName.get_35(rawItem.enchant)
        if(enchant) {
          item.enchant = enchant(item)
        }
      }

      if(rawItem.temporaryEnhancement) {
        const tmpEnchant = tbcsim.data.TempEnchants.byName.get_35(rawItem.temporaryEnhancement)
        if(tmpEnchant) {
          item.temporaryEnhancement = tmpEnchant(item)
        }
      }

      return item
    })
    dispatch({ type: 'loadCharacterPreset', value: clone })
    setIsOpen(false)

    // Track preset + class interest
    if(window.gtag) {
      window.gtag('event', 'load', {
        'event_category': 'characterPreset',
        'event_label': clone.description || 'unknown preset',
        'event_value': 1
      });
    }
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
          <Dropdown.Menu title="Hunter">
            {presetsFor("hunter")}
          </Dropdown.Menu>
          <Dropdown.Menu title="Shaman">
            {presetsFor("shaman")}
          </Dropdown.Menu>
          <Dropdown.Menu title="Warlock">
            {presetsFor("warlock")}
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
