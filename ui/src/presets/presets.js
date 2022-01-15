import React, { useState } from 'react';
import { Col, Dropdown, Row, Button, Uploader, Notification } from 'rsuite';
import _ from 'lodash';
import filesaver from 'file-saver';

import { classes, allEpCategories, targetTypes } from '../data/constants';
import * as impex from './importexport';
import EPOptions from '../ep/ep_options';

import hunterBmPreraid from './samples/hunter_bm_preraid.yml'
import hunterBmPhase1 from './samples/hunter_bm_phase1.yml'
import hunterBmPhase2 from './samples/hunter_bm_phase2.yml'
import hunterBmPhase3 from './samples/hunter_bm_phase3.yml'
import hunterSurvPreraid from './samples/hunter_surv_preraid.yml'
import hunterSurvPhase1 from './samples/hunter_surv_phase1.yml'
import hunterSurvPhase2 from './samples/hunter_surv_phase2.yml'
import hunterSurvPhase3 from './samples/hunter_surv_phase3.yml'
import mageArcanePreraid from './samples/mage_arcane_preraid.yml'
import mageArcanePhase1 from './samples/mage_arcane_phase1.yml'
import mageArcanePhase2 from './samples/mage_arcane_phase2.yml'
import mageArcanePhase3 from './samples/mage_arcane_phase3.yml'
import mageFirePreraid from './samples/mage_fire_preraid.yml'
import mageFirePhase1 from './samples/mage_fire_phase1.yml'
import mageFirePhase2 from './samples/mage_fire_phase2.yml'
import mageFirePhase3 from './samples/mage_fire_phase3.yml'
import mageFrostPreraid from './samples/mage_frost_preraid.yml'
import mageFrostPhase1 from './samples/mage_frost_phase1.yml'
import mageFrostPhase2 from './samples/mage_frost_phase2.yml'
import mageFrostPhase3 from './samples/mage_frost_phase3.yml'
import priestShadowPreraid from './samples/priest_shadow_preraid.yml'
import priestShadowPhase1 from './samples/priest_shadow_phase1.yml'
import priestShadowPhase2 from './samples/priest_shadow_phase2.yml'
import priestShadowPhase3 from './samples/priest_shadow_phase3.yml'
import rogueAssassinationPreraid from './samples/rogue_assassination_preraid.yml'
import rogueAssassinationPhase1 from './samples/rogue_assassination_phase1.yml'
import rogueAssassinationPhase2 from './samples/rogue_assassination_phase2.yml'
import rogueCombatPreraid from './samples/rogue_combat_preraid.yml'
import rogueCombatPhase1 from './samples/rogue_combat_phase1.yml'
import rogueCombatPhase2 from './samples/rogue_combat_phase2.yml'
import rogueCombatPhase3 from './samples/rogue_combat_phase3.yml'
import rogueCombatPhase3Glaives from './samples/rogue_combat_phase3_glaives.yml'
import shamanElePreraid from './samples/shaman_ele_preraid.yml'
import shamanElePhase1 from './samples/shaman_ele_phase1.yml'
import shamanElePhase2 from './samples/shaman_ele_phase2.yml'
import shamanElePhase3 from './samples/shaman_ele_phase3.yml'
import shamanEnhSubElePreraid from './samples/shaman_enh_subele_preraid.yml'
import shamanEnhSubElePhase1 from './samples/shaman_enh_subele_phase1.yml'
import shamanEnhSubElePhase2 from './samples/shaman_enh_subele_phase2.yml'
import shamanEnhSubElePhase3 from './samples/shaman_enh_subele_phase3.yml'
import shamanEnhSubRestoPreraid from './samples/shaman_enh_subresto_preraid.yml'
import shamanEnhSubRestoPhase1 from './samples/shaman_enh_subresto_phase1.yml'
import shamanEnhSubRestoPhase2 from './samples/shaman_enh_subresto_phase2.yml'
import shamanEnhSubRestoPhase3 from './samples/shaman_enh_subresto_phase3.yml'
import shamanEnhSubRestoPreraidAnniMh from './samples/shaman_enh_subresto_preraid_annihilator_mh.yml'
import shamanEnhSubRestoPhase1AnniMh from './samples/shaman_enh_subresto_phase1_annihilator_mh.yml'
import shamanEnhSubRestoPhase2AnniMh from './samples/shaman_enh_subresto_phase2_annihilator_mh.yml'
import shamanEnhSubRestoPhase3AnniMh from './samples/shaman_enh_subresto_phase3_annihilator_mh.yml'
import warlockAfflictionRuinPreraid from './samples/warlock_affliction_ruin_preraid.yml'
import warlockAfflictionRuinPhase1 from './samples/warlock_affliction_ruin_phase1.yml'
import warlockAfflictionRuinPhase2 from './samples/warlock_affliction_ruin_phase2.yml'
import warlockAfflictionRuinPhase3 from './samples/warlock_affliction_ruin_phase3.yml'
import warlockDestructionFirePreraid from './samples/warlock_destruction_fire_preraid.yml'
import warlockDestructionFirePhase1 from './samples/warlock_destruction_fire_phase1.yml'
import warlockDestructionFirePhase2 from './samples/warlock_destruction_fire_phase2.yml'
import warlockDestructionFirePhase3 from './samples/warlock_destruction_fire_phase3.yml'
import warlockDestructionShadowPreraid from './samples/warlock_destruction_shadow_preraid.yml'
import warlockDestructionShadowPhase1 from './samples/warlock_destruction_shadow_phase1.yml'
import warlockDestructionShadowPhase2 from './samples/warlock_destruction_shadow_phase2.yml'
import warlockDestructionShadowPhase3 from './samples/warlock_destruction_shadow_phase3.yml'
import warriorArmsPreraid from './samples/warrior_arms_preraid.yml'
import warriorArmsPhase1 from './samples/warrior_arms_phase1.yml'
import warriorArmsPhase2 from './samples/warrior_arms_phase2.yml'
import warriorArmsPhase3 from './samples/warrior_arms_phase3.yml'
import warriorFuryPreraid from './samples/warrior_fury_preraid.yml'
import warriorFuryPhase1 from './samples/warrior_fury_phase1.yml'
import warriorFuryPhase2 from './samples/warrior_fury_phase2.yml'
import warriorFuryPhase3 from './samples/warrior_fury_phase3.yml'
import warriorFuryPhase3Glaives from './samples/warrior_fury_phase3_glaives.yml'
import warriorKebabPhase2 from './samples/warrior_kebab_phase2.yml'
import warriorKebabPhase3 from './samples/warrior_kebab_phase3.yml'
import warriorKebabPhase3Glaives from './samples/warrior_kebab_phase3_glaives.yml'
import warriorProtectionPhase2 from './samples/warrior_protection_phase2.yml'
import warriorProtectionPhase3 from './samples/warrior_protection_phase3.yml'
import warriorProtectionPhase3Glaives from './samples/warrior_protection_phase3_glaives.yml'

import * as tbcsim from 'tbcsim';

const presets = {
  hunter: {
    preraid: [
      hunterBmPreraid,
      hunterSurvPreraid
    ],
    phase1: [
      hunterBmPhase1,
      hunterSurvPhase1
    ],
    phase2: [
      hunterBmPhase2,
      hunterSurvPhase2
    ],
    phase3: [
      hunterBmPhase3,
      hunterSurvPhase3
    ]
  },
  mage: {
    preraid: [
      mageArcanePreraid,
      mageFirePreraid,
      mageFrostPreraid
    ],
    phase1: [
      mageArcanePhase1,
      mageFirePhase1,
      mageFrostPhase1
    ],
    phase2: [
      mageArcanePhase2,
      mageFirePhase2,
      mageFrostPhase2
    ],
    phase3: [
      mageArcanePhase3,
      mageFirePhase3,
      mageFrostPhase3
    ]
  },
  priest: {
    preraid: [
      priestShadowPreraid,
    ],
    phase1: [
      priestShadowPhase1,
    ],
    phase2: [
      priestShadowPhase2,
    ],
    phase3: [
      priestShadowPhase3,
    ]
  },
  rogue: {
    preraid: [
      rogueAssassinationPreraid,
      rogueCombatPreraid
    ],
    phase1: [
      rogueAssassinationPhase1,
      rogueCombatPhase1
    ],
    phase2: [
      rogueAssassinationPhase2,
      rogueCombatPhase2
    ],
    phase3: [
      rogueCombatPhase3,
      rogueCombatPhase3Glaives
    ]
  },
  shaman: {
    preraid: [
      shamanElePreraid,
      shamanEnhSubElePreraid,
      shamanEnhSubRestoPreraid,
      shamanEnhSubRestoPreraidAnniMh
    ],
    phase1: [
      shamanElePhase1,
      shamanEnhSubElePhase1,
      shamanEnhSubRestoPhase1,
      shamanEnhSubRestoPhase1AnniMh
    ],
    phase2: [
      shamanElePhase2,
      shamanEnhSubElePhase2,
      shamanEnhSubRestoPhase2,
      shamanEnhSubRestoPhase2AnniMh
    ],
    phase3: [
      shamanElePhase3,
      shamanEnhSubElePhase3,
      shamanEnhSubRestoPhase3,
      shamanEnhSubRestoPhase3AnniMh
    ]
  },
  warlock: {
    preraid: [
      warlockDestructionFirePreraid,
      warlockDestructionShadowPreraid,
      warlockAfflictionRuinPreraid
    ],
    phase1: [
      warlockDestructionFirePhase1,
      warlockDestructionShadowPhase1,
      warlockAfflictionRuinPhase1
    ],
    phase2: [
      warlockDestructionFirePhase2,
      warlockDestructionShadowPhase2,
      warlockAfflictionRuinPhase2
    ],
    phase3: [
      warlockDestructionFirePhase3,
      warlockDestructionShadowPhase3,
      warlockAfflictionRuinPhase3
    ]
  },
  warrior: {
    preraid: [
      warriorArmsPreraid,
      warriorFuryPreraid
    ],
    phase1: [
      warriorArmsPhase1,
      warriorFuryPhase1
    ],
    phase2: [
      warriorArmsPhase2,
      warriorFuryPhase2,
      warriorKebabPhase2,
      warriorProtectionPhase2
    ],
    phase3: [
      warriorArmsPhase3,
      warriorFuryPhase3,
      warriorFuryPhase3Glaives,
      warriorKebabPhase3,
      warriorKebabPhase3Glaives,
      warriorProtectionPhase3,
      warriorProtectionPhase3Glaives
    ]
  }
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

function PhaseSelect({ phase, dispatch }) {
  if(phase == null) return null;

  const allPhases = [1, 2, 3, 4, 5]

  function onSelect(phase) {
    dispatch({ type: 'phase', value: phase })
  }

  return (
    <>
      <Dropdown title="Item Filter">
        {allPhases.map(phase => {
          return <Dropdown.Item key={phase} eventKey={phase} onSelect={onSelect}>Phase {phase}</Dropdown.Item>
        })}
      </Dropdown>
      <span>Phase {phase}</span>
    </>
  );
}

function EpSelect({ epCategoryKey, dispatch }) {
  if(epCategoryKey == null) return null;

  const epCategoryEntry = allEpCategories.find(epc => epc.key == epCategoryKey)
  if(epCategoryEntry == null) return null;

  const epCategoryName = epCategoryEntry.name;

  function onSelect(epCategory) {
    dispatch({ type: 'character.epCategory', value: epCategory })
  }

  return (
    <>
      <Dropdown title="EP Category">
        {allEpCategories.map(epCategory => {
          return <Dropdown.Item key={epCategory.key} eventKey={epCategory.key} onSelect={onSelect}>{epCategory.name}</Dropdown.Item>
        })}
      </Dropdown>
      <span>{epCategoryName}</span>
    </>
  );
}

function TargetTypeSelect({ targetTypeOrdinal, dispatch }) {
  if(targetTypeOrdinal == null) return null;

  function onSelect(ordinal) {
    dispatch({ type: 'simOptions.targetType', value: ordinal })
  }

  const targetTypeName = (targetTypes.find(it => it.key === targetTypeOrdinal) || {}).name;
  return (
    <>
      <Dropdown title="Target Type">
        {targetTypes.map(type => {
          return <Dropdown.Item key={type.key} eventKey={type.key} onSelect={onSelect}>{type.name}</Dropdown.Item>
        })}
      </Dropdown>
      <span>{targetTypeName}</span>
    </>
  );
}

export default ({ character, phase, raidBuffs, raidDebuffs, simOptions, epOptions, dispatch }) => {
  const [isOpen, setIsOpen] = useState(false);

  function Import({ dispatch }) {
    const importPreset = (file) => {
      try {
        const reader = new FileReader()

        reader.onload = (evt) => {
          try {
            const presetObj = impex.importPreset(evt.target.result)
            presetObj['filename'] = file.name
            return loadPreset(presetObj, dispatch);
          } catch(e) {
            Notification['error']({
              title: 'Import Error',
              duration: 5000,
              description: (
                <div>
                  <p>Error processing uploaded file, please make sure it is a valid preset YAML file.</p>
                </div>
              )
            });
          }
        };

        reader.readAsText(file.blobFile)
      } catch(e) {
        Notification['error']({
          title: 'Import Error',
          duration: 5000,
          description: (
            <div>
              <p>Error uploading file, please try again.</p>
            </div>
          )
        });
      }
    }

    return <Uploader draggable={true} accept='yaml, yml' fileListVisible={false} disabledFileItem={true} onUpload={importPreset} />
  }

  function Export({ dispatch }) {
    const exportPreset = () => {
      try {
        const filename = character.filename || "exported-preset.yaml";
        const exported = impex.exportPreset(character, raidBuffs, raidDebuffs);
        const blob = new Blob([exported], {type: "text/yaml;charset=utf-8"});
        filesaver.saveAs(blob, filename);
      } catch(e) {
        Notification['error']({
          title: 'Export Error',
          duration: 5000,
          description: (
            <div>
              <p>Error saving file, please try again.</p>
            </div>
          )
        });
      }
    }

    return <Button disabled={!character.class} onClick={exportPreset}>Export</Button>
  }

  function loadPreset(presetObj, dispatch) {
    const clone = JSON.parse(JSON.stringify(presetObj))
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

  function onSelect(key, evt) {
    const [klass, category, idx] = key.split('-');
    return loadPreset(presets[klass][category][idx], dispatch);
  }

  function presetsFor(klass, category) {
    return <>
      {(presets[klass][category] || []).filter(it => it && (it.epCategory === category)).map((p, idx) => {
        const key = `${klass}-${category}-${idx}`;
        return <Dropdown.Item key={key} eventKey={key} onSelect={onSelect}>{p.description}</Dropdown.Item>
      })}
    </>
  }

  return (
    <Row style={{padding: '10px 0px', fontWeight: 800}}>
      <Col style={{ display: 'inline-block' }}>
        <Dropdown title='Presets'
          onMouseEnter={() => setIsOpen(true)}
          onMouseLeave={() => setIsOpen(false)}
          open={isOpen}
        >
          <Dropdown.Menu title='Hunter'>
            <Dropdown.Menu key={'phase3'} title='Phase 3'>
              {presetsFor('hunter', 'phase3')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase2'} title='Phase 2'>
              {presetsFor('hunter', 'phase2')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase1'} title='Phase 1'>
              {presetsFor('hunter', 'phase1')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'preraid'} title='Pre-raid'>
              {presetsFor('hunter', 'preraid')}
            </Dropdown.Menu>
          </Dropdown.Menu>
          <Dropdown.Menu title='Mage'>
            <Dropdown.Menu key={'phase3'} title='Phase 3'>
              {presetsFor('mage', 'phase3')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase2'} title='Phase 2'>
              {presetsFor('mage', 'phase2')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase1'} title='Phase 1'>
              {presetsFor('mage', 'phase1')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'preraid'} title='Pre-raid'>
              {presetsFor('mage', 'preraid')}
            </Dropdown.Menu>
          </Dropdown.Menu>
          <Dropdown.Menu title='Priest'>
            <Dropdown.Menu key={'phase3'} title='Phase 3'>
              {presetsFor('priest', 'phase3')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase2'} title='Phase 2'>
              {presetsFor('priest', 'phase2')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase1'} title='Phase 1'>
              {presetsFor('priest', 'phase1')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'preraid'} title='Pre-raid'>
              {presetsFor('priest', 'preraid')}
            </Dropdown.Menu>
          </Dropdown.Menu>
          <Dropdown.Menu title='Rogue'>
            <Dropdown.Menu key={'phase3'} title='Phase 3'>
              {presetsFor('rogue', 'phase3')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase2'} title='Phase 2'>
              {presetsFor('rogue', 'phase2')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase1'} title='Phase 1'>
              {presetsFor('rogue', 'phase1')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'preraid'} title='Pre-raid'>
              {presetsFor('rogue', 'preraid')}
            </Dropdown.Menu>
          </Dropdown.Menu>
          <Dropdown.Menu title='Shaman'>
            <Dropdown.Menu key={'phase3'} title='Phase 3'>
              {presetsFor('shaman', 'phase3')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase2'} title='Phase 2'>
              {presetsFor('shaman', 'phase2')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase1'} title='Phase 1'>
              {presetsFor('shaman', 'phase1')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'preraid'} title='Pre-raid'>
              {presetsFor('shaman', 'preraid')}
            </Dropdown.Menu>
          </Dropdown.Menu>
          <Dropdown.Menu title='Warlock'>
            <Dropdown.Menu key={'phase3'} title='Phase 3'>
              {presetsFor('warlock', 'phase3')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase2'} title='Phase 2'>
              {presetsFor('warlock', 'phase2')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase1'} title='Phase 1'>
              {presetsFor('warlock', 'phase1')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'preraid'} title='Pre-raid'>
              {presetsFor('warlock', 'preraid')}
            </Dropdown.Menu>
          </Dropdown.Menu>
          <Dropdown.Menu title='Warrior'>
            <Dropdown.Menu key={'phase3'} title='Phase 3'>
              {presetsFor('warrior', 'phase3')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phases'} title='Phase 2'>
              {presetsFor('warrior', 'phase2')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'phase1'} title='Phase 1'>
              {presetsFor('warrior', 'phase1')}
            </Dropdown.Menu>
            <Dropdown.Menu key={'preraid'} title='Pre-raid'>
              {presetsFor('warrior', 'preraid')}
            </Dropdown.Menu>
          </Dropdown.Menu>
        </Dropdown>

        {character && character.description ?
          <span>{character.description}</span> :
          <span>Please select a preset</span>
        }
      </Col>
      <Col style={{ display: 'inline-block', marginLeft: 10 }}>
        <RaceSelect character={character} dispatch={dispatch} />
      </Col>
      <Col style={{ display: 'inline-block', marginLeft: 10 }}>
        <PhaseSelect phase={phase} dispatch={dispatch} />
      </Col>
      <Col style={{ display: 'inline-block', marginLeft: 10 }}>
        <EpSelect epCategoryKey={character && character.epCategory} dispatch={dispatch} />
      </Col>
      <Col style={{ display: 'inline-block', marginLeft: 10 }}>
        <TargetTypeSelect targetTypeOrdinal={simOptions && simOptions.targetType} dispatch={dispatch} />
      </Col>
      <Col style={{ display: 'inline-block', marginLeft: 10 }}>
        <Import dispatch={dispatch} />
      </Col>
      <Col style={{ display: 'inline-block', marginLeft: 10 }}>
        <Export dispatch={dispatch} />
      </Col>
      <Col style={{ display: 'inline-block', marginLeft: 10 }}>
        <EPOptions epOptions={epOptions} dispatch={dispatch} />
      </Col>
    </Row>
  )
}
