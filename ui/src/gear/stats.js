import React, { useState } from 'react';
import { Col, Container, Dropdown, Row } from 'rsuite';

import * as tbcsim from 'tbcsim';

function BaseStats({ simParticipant: sp }) {
  return (
    <Col>
      <Row>
        <Col xs={12}>Strength:</Col>
        <Col xs={12}>{sp.strength_0()}</Col>
      </Row>
      <Row>
        <Col xs={12}>Agility:</Col>
        <Col xs={12}>{sp.agility_0()}</Col>
      </Row>
      <Row>
        <Col xs={12}>Stamina:</Col>
        <Col xs={12}>{sp.stamina_0()}</Col>
      </Row>
      <Row>
        <Col xs={12}>Intellect:</Col>
        <Col xs={12}>{sp.intellect_0()}</Col>
      </Row>
      <Row>
        <Col xs={12}>Spirit:</Col>
        <Col xs={12}>{sp.spirit_0()}</Col>
      </Row>
    </Col>
  )
}

function MeleeStats({ simParticipant: sp }) {
  let damageMhLow = '0.0'
  let damageMhHigh = '0.0';
  let damageOhLow = '0.0'
  let damageOhHigh = '0.0';
  let speedMh = '-';
  let speedOh = '-';

  const hasMainhand = sp && sp.character && sp.character.gear && sp.character.gear.mainHand && sp.character.gear.mainHand.itemClass;
  if(hasMainhand) {
    const dmgFromAp = tbcsim.mechanics.Melee.apToDamage(sp, sp.attackPower_0(), sp.character.gear.mainHand);
    damageMhLow = (sp.character.gear.mainHand.minDmg + dmgFromAp).toFixed(1);
    damageMhHigh = (sp.character.gear.mainHand.maxDmg + dmgFromAp).toFixed(1);
    speedMh = (sp.character.gear.mainHand.speed / 1000.0 / sp.meleeHasteMultiplier()).toFixed(2);
  }

  const hasOffhand = sp && sp.character && sp.character.gear && sp.character.gear.offHand && sp.character.gear.offHand.itemClass;
  if(hasOffhand) {
    const dmgFromAp = tbcsim.mechanics.Melee.apToDamage(sp, sp.attackPower_0(), sp.character.gear.offHand);
    damageOhLow = ((sp.character.gear.offHand.minDmg + dmgFromAp) / 2).toFixed(1);
    damageOhHigh = ((sp.character.gear.offHand.maxDmg + dmgFromAp) / 2).toFixed(1);
    speedOh = (sp.character.gear.offHand.speed / 1000.0 / sp.meleeHasteMultiplier()).toFixed(2);
  }

  return (
    <Col>
      {hasMainhand ? <Row>
        <Col xs={12}>Damage (MH):</Col>
        <Col xs={12}>{damageMhLow}-{damageMhHigh} ({speedMh})</Col>
      </Row> : null}
      {hasOffhand ? <Row>
        <Col xs={12}>Damage (OH):</Col>
        <Col xs={12}>{damageOhLow}-{damageOhHigh} ({speedOh})</Col>
      </Row> : null}
      <Row>
        <Col xs={12}>Attack Power:</Col>
        <Col xs={12}>{sp.attackPower_0()}</Col>
      </Row>
      <Row>
        <Col xs={12}>Hit %:</Col>
        <Col xs={12}>{sp.meleeHitPct().toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>Crit %:</Col>
        <Col xs={12}>{sp.meleeCritPct().toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>Expertise %:</Col>
        <Col xs={12}>{sp.expertisePct().toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>Armor Pen:</Col>
        <Col xs={12}>{sp.armorPen_0()}</Col>
      </Row>
    </Col>
  )
}

function RangedStats({ simParticipant: sp }) {
  const damage = 0.0;
  const speed = 1.0;

  return (
    <Col>
      <Row>
        <Col xs={12}>Damage:</Col>
        <Col xs={12}>{damage} ({speed})</Col>
      </Row>
      <Row>
        <Col xs={12}>Attack Power:</Col>
        <Col xs={12}>{sp.rangedAttackPower_0()}</Col>
      </Row>
      <Row>
        <Col xs={12}>Hit %:</Col>
        <Col xs={12}>{sp.meleeHitPct().toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>Crit %:</Col>
        <Col xs={12}>{sp.meleeCritPct().toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>Armor Pen:</Col>
        <Col xs={12}>{sp.armorPen_0()}</Col>
      </Row>
    </Col>
  )
}

function SpellStats({ simParticipant: sp }) {
  return (
    <Col>
      <Row>
        <Col xs={12}>Spell Damage:</Col>
        <Col>{sp.spellDamage_0()}</Col>
      </Row>
      <Row>
        <Col xs={12}>Spell Pen:</Col>
        <Col>{sp.stats._spellPen}</Col>
      </Row>
      <Row>
        <Col xs={12}>Hit %:</Col>
        <Col>{sp.spellHitPct().toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>Crit %:</Col>
        <Col>{sp.spellCritPct().toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>Haste %:</Col>
        <Col>{(sp.spellHasteMultiplier_0() - 1).toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>MP5:</Col>
        <Col>{sp.stats._manaPer5Seconds}</Col>
      </Row>
    </Col>
  )
}

function DefensiveStats({ simParticipant: sp }) {
  const armor = sp.armor_0();
  const armorMit = ((armor / (armor + (467.5 * 70 - 22167.5))) * 100).toFixed(2)

  return (
    <Col>
      <Row>
        <Col xs={12}>Armor:</Col>
        <Col xs={12}>{armor} ({armorMit}%)</Col>
      </Row>
      <Row>
        <Col xs={12}>Defense:</Col>
        <Col xs={12}>{350 + sp.defenseSkill()}</Col>
      </Row>
      <Row>
        <Col xs={12}>Dodge %:</Col>
        <Col xs={12}>{sp.dodgePct_0().toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>Parry %:</Col>
        <Col xs={12}>{sp.parryPct_0().toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>Block %:</Col>
        <Col xs={12}>{sp.blockPct().toFixed(2)}%</Col>
      </Row>
      <Row>
        <Col xs={12}>Resilience:</Col>
        <Col xs={12}>{sp.resiliencePct().toFixed(2)}%</Col>
      </Row>
    </Col>
  )
}

export default function({ state }) {
  const [dropdownLeft, setDropdownLeft] = useState('baseStats')
  const [dropdownRight, setDropdownRight] = useState('meleeStats')

  if(!state.character.class) return null;

  const simConfig = state.makeSimConfig();
  const simParticipant = new tbcsim.sim.SimParticipant(
    simConfig.character,
    simConfig.rotation,
    // Shim any parts of the sim object we need
    {
      _elapsedTimeMs: 0,
      getExpirationTick: function() { return 0; },
      _target_0: {
        addDebuff: function() {},
      }
    }
  ).init()

  const panels = {
    baseStats: {
      title: "Base Stats",
      component: BaseStats
    },
    meleeStats: {
      title: "Melee",
      component: MeleeStats
    },
    rangedStats: {
      title: "Ranged",
      component: RangedStats
    },
    spellStats: {
      title: "Spell",
      component: SpellStats
    },
    defensiveStats: {
      title: "Defense",
      component: DefensiveStats
    }
  }

  function onSelect(value, setter) {
    setter(value)
  }

  const titleLeft = panels[dropdownLeft].title;
  const titleRight = panels[dropdownRight].title;

  function renderOptions(setter) {
    return (
      <>
        {Object.entries(panels).map(([key, value]) => {
          return (
            <Dropdown.Item key={key} eventKey={key} onSelect={key => onSelect(key, setter)}>
              {value.title}
            </Dropdown.Item>
          )
        })}
      </>
    )
  }

  const PanelLeft = panels[dropdownLeft].component
  const PanelRight = panels[dropdownRight].component

  return (
    <Row style={{ maxHeight: 400, maxWidth: 500, margin: 'auto' }}>
      <Col xs={12}>
        <Dropdown trigger={['click', 'click']} title={titleLeft} activeKey={dropdownLeft}>
          {renderOptions(setDropdownLeft)}
        </Dropdown>
        <Container>
          <PanelLeft simParticipant={simParticipant} />
        </Container>
      </Col>
      <Col xs={12}>
        <Dropdown trigger={['click', 'click']} title={titleRight} activeKey={dropdownRight}>
          {renderOptions(setDropdownRight)}
        </Dropdown>
        <Container>
          <PanelRight simParticipant={simParticipant} />
        </Container>
      </Col>
    </Row>
  )
}
