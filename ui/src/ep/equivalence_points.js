import React from 'react';
import { Container, Content, Grid, Row, Col, Panel, Message } from 'rsuite';

import epData from './data/ep_all.json';

const statDisplayNames = {
  attackPower: 'Attack Power',
  rangedAttackPower: 'Ranged Attack Power',
  agility: 'Agility',
  intellect: 'Intellect',
  strength: 'Strength',
  physicalCritRating: 'Crit Rating',
  physicalHasteRating: 'Haste Rating',
  physicalHitRating: 'Hit Rating',
  armorPen: 'Armor Pen',
  expertiseRating: 'Expertise Rating',
  spellDamage: 'Spell Damage',
  spellCritRating: 'Crit Rating',
  spellHasteRating: 'Haste Rating',
  spellHitRating: 'Hit Rating'
}

const bannerTitle = 'EP values are very beta!'
function bannerMsg() {
  return (
    <div>
      <p>These values have not been cross-referenced with other work, and may be anywhere from exactly right to wildly incorrect</p>
      <p>Please consume these values with an appropriate amount of sodium</p>
    </div>
  );
}

function HowItWorks() {
  return (
    <Container style={{ marginTop: 20, marginBottom: 20 }}>
      <Row>
        <h5>How are these values calculated?</h5>
        <ul>
          <li>Each specialization is simulated for 10,000 iterations at 10ms resolution, with full raid buffs</li>
          <li>The gear, buff and rotation setups for each of these specializations are the presets selectable on the simulator page</li>
          <li>EP values are per-tier.  Each preset has a tier-specific BIS or BIS-adjacent gear set (if it isn't BIS/BIS-adjacent, please file a bug!)</li>
        </ul>
        <h5>Notes and Caveats</h5>
        <ul>
          <li>Hit EPs are only calculated for dual-wielding classes and casters.  For others, it's not interesting - it's the best stat until cap, every time.  Later tiers will likely not include casters either, but it's not trivial to hit cap in many lower gear level situations</li>
          <li>Armor pen scaling is not linear, and the EP values listed are the values relative to the preset's current level of armor pen</li>
        </ul>
      </Row>
    </Container>
  );
}

function WrapperEpPanel({ name, collapsible=true, children }) {
  return (
    <Row>
      <Col xs={24}>
        <Panel
          collapsible={collapsible}
          defaultExpanded={true}
          header={name}
          bordered
          style={{ width: '100%' }}
        >{children}</Panel>
      </Col>
    </Row>
  )
}

function SpecEpPanel({ name, specKey, category }) {
  const specData = ((epData[category] || {})[specKey]) || {}
  return (
    <Panel header={name} bordered style={{ display: 'inline-block', width: 400, marginLeft: 20 }}>
      <Col>
        {Object.entries(specData).map(([key, value]) => {
          return (
            <Row key={key}>
              <Col xs={12}>{statDisplayNames[key] || key}:</Col>
              <Col xs={12}>{value}</Col>
            </Row>
          )
        })}
      </Col>
    </Panel>
  )
}

export default function() {
  return (
    <Content style={{ padding: '20px' }}>
      <Grid fluid={true}>
        <Message type='warning' title={bannerTitle} description={bannerMsg()} />
        <HowItWorks />
        <WrapperEpPanel name='Pre-raid'>
          <WrapperEpPanel name='Hunter'>
            <SpecEpPanel name='Beast Mastery' specKey='hunter_bm' category='pre_raid' />
            <SpecEpPanel name='Survival' specKey='hunter_surv' category='pre_raid' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Shaman'>
            <SpecEpPanel name='Enhancement' specKey='shaman_enh' category='pre_raid' />
            <SpecEpPanel name='Elemental' specKey='shaman_ele' category='pre_raid' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Warlock'>
            <SpecEpPanel name='Affliction (Ruin)' specKey='warlock_affliction_ruin' category='pre_raid' />
            <SpecEpPanel name='Affliction (UA)' specKey='warlock_affliction_ua' category='pre_raid' />
            <SpecEpPanel name='Destruction (Fire)' specKey='warlock_destro_fire' category='pre_raid' />
            <SpecEpPanel name='Destruction (Shadow)' specKey='warlock_destro_shadow' category='pre_raid' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Warrior'>
            <SpecEpPanel name='Arms' specKey='warrior_arms' category='pre_raid' />
            <SpecEpPanel name='Fury' specKey='warrior_fury' category='pre_raid' />
          </WrapperEpPanel>
        </WrapperEpPanel>
      </Grid>
    </Content>
  )
}
