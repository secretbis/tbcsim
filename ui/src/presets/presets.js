import React, { useState } from 'react';
import { Col, Dropdown, Row } from 'rsuite';
import _ from 'lodash';

import { classes } from '../data/constants';

import hunterBmPreraid from './samples/hunter_bm_preraid.yml'
import hunterSurvPreraid from './samples/hunter_surv_preraid.yml'
import mageArcanePreraid from './samples/mage_arcane_preraid.yml'
import mageFirePreraid from './samples/mage_fire_preraid.yml'
import mageFrostPreraid from './samples/mage_frost_preraid.yml'
import rogueAssassinationPreraid from './samples/rogue_assassination_preraid.yml'
import rogueCombatPreraid from './samples/rogue_combat_preraid.yml'
import shamanElePreraid from './samples/shaman_ele_preraid.yml'
import shamanEnhSubElePreraid from './samples/shaman_enh_subele_preraid.yml'
import shamanEnhSubRestoPreraid from './samples/shaman_enh_subresto_preraid.yml'
import shamanEnhSubRestoPreraidAnniMh from './samples/shaman_enh_subresto_preraid_annihilator_mh.yml'
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
  mage: [
    mageArcanePreraid,
    mageFirePreraid,
    mageFrostPreraid
  ],
  rogue: [
    rogueAssassinationPreraid,
    rogueCombatPreraid
  ],
  shaman: [
    shamanElePreraid,
    shamanEnhSubElePreraid,
    shamanEnhSubRestoPreraid,
    shamanEnhSubRestoPreraidAnniMh,
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

function RaceSelect({ character, dispatch }) {
  if(!character || !character.class) return null;

  const classData = classes[character.class.toLowerCase()]
  const racesForClass = classData && classData.races;
  if(!racesForClass) return null;

  function onSelect(race) {
    dispatch({ type: 'character.race', value: race })
  }

  return (
    <>
      <Dropdown title="Race">
        {racesForClass.map(race => {
          return <Dropdown.Item key={race} eventKey={race} onSelect={onSelect}>{race}</Dropdown.Item>
        })}
      </Dropdown>
      <span>{character.race}</span>
    </>
  );
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

      if(rawItem.tempEnchant) {
        const tmpEnchant = tbcsim.data.TempEnchants.byName.get_35(rawItem.tempEnchant)
        if(tmpEnchant) {
          item.tempEnchant = tmpEnchant(item)
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
      <Col style={{ display: 'inline-block' }}>
        <Dropdown title="Presets"
          onMouseEnter={() => setIsOpen(true)}
          onMouseLeave={() => setIsOpen(false)}
          open={isOpen}
        >
          <Dropdown.Menu title="Hunter">
            {presetsFor("hunter")}
          </Dropdown.Menu>
          <Dropdown.Menu title="Mage">
            {presetsFor("mage")}
          </Dropdown.Menu>
          <Dropdown.Menu title="Rogue">
            {presetsFor("rogue")}
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
          <span>{value.description}</span> :
          <span>Please select a preset</span>
        }
      </Col>
      <Col style={{ display: 'inline-block', marginLeft: 10 }}>
        <RaceSelect character={value} dispatch={dispatch} />
      </Col>
    </Row>
  )
}
