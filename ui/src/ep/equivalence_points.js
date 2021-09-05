import React from 'react';
import _ from 'lodash';
import { Container, Content, Grid, Row, Col, Panel, Message } from 'rsuite';

import { allStats, statDisplayNames } from './ep_stats';
import epData from './data/ep_all.json';

const bannerTitle = 'EP values are very beta!'
function bannerMsg() {
  return (
    <div>
      <p>These values have not been cross-referenced with other work, and may be anywhere from exactly right to wildly incorrect.</p>
      <p>These values are very likely to change as TBC behavior is tested further, and sim issues are resolved.</p>
      <p>Please consume these values with an appropriate amount of sodium!</p>
    </div>
  );
}

function HowItWorks() {
  return (
    <Container style={{ marginTop: 20, marginBottom: 20 }}>
      <Row>
        <h5>How are these values calculated?</h5>
        <ul>
          <li>Each specialization is simulated for 15,000 iterations at 10ms resolution, with full raid buffs</li>
          <li>The gear, buff and rotation setups for each of these specializations are the presets selectable on the simulator page</li>
          <li>EP values are per-tier.  Each preset has a tier-specific BIS or BIS-adjacent gear set (if it isn't BIS/BIS-adjacent, please file a bug!)</li>
        </ul>
        <h5>Notes and Caveats</h5>
        <ul>
          <li>Hit EPs are only calculated for dual-wielding classes and some casters.  For others, it's the best stat until cap</li>
          <li>Armor pen scaling is not linear, and the EP values listed are the values relative to the preset's current level of armor pen.  More armor pen will increase the value of each point of armor pen</li>
          <li>Item procs and set bonuses are not currently included in total item EP when selecting items in the UI (Procs and sets are fully modeled in the sim)</li>
          <li>1 weapon DPS is worth 14 attack power, by definition.  Item EPs include this for melee specs, and also include ranged weapon DPS for hunters.  Weapon speed is relevant, of course, but the difference there is too dynamic and needs to be simmed</li>
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
  const specData = _.get(epData, `categories[${category}][${specKey}]`, {})
  return (
    <Panel header={name} bordered style={{ display: 'inline-block', width: 400, marginLeft: 20 }}>
      <Col>
        {allStats.map((key, idx) => {
          if(!_.has(specData, key)) return null

          const name = statDisplayNames[key]

          if(!name) return null

          const value = (specData[key] || 0).toFixed(2);

          // Add some spacing for the first value (base EPs) and the start of the gems
          const marginTop = key === 'redSocket' ? 15 : 0
          const marginBottom = idx <= 2 ? 15 : 0

          return (
            <Row key={key} style={{ marginTop, marginBottom }}>
              <Col xs={12}>{name}:</Col>
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
        <Message type='warning' title={bannerTitle} description={bannerMsg()} closable />
        <HowItWorks />
        <WrapperEpPanel name='Phase 2'>
          <WrapperEpPanel name='Hunter'>
            <SpecEpPanel name='Beast Mastery' specKey='hunter_bm' category='phase2' />
            <SpecEpPanel name='Survival' specKey='hunter_surv' category='phase2' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Mage'>
            <SpecEpPanel name='Arcane' specKey='mage_arcane' category='phase2' />
            <SpecEpPanel name='Fire' specKey='mage_fire' category='phase2' />
            <SpecEpPanel name='Frost' specKey='mage_frost' category='phase2' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Rogue'>
            <SpecEpPanel name='Assassination' specKey='rogue_assassination' category='phase2' />
            <SpecEpPanel name='Combat' specKey='rogue_combat' category='phase2' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Shaman'>
            <SpecEpPanel name='Enhancement' specKey='shaman_enh' category='phase2' />
            <SpecEpPanel name='Elemental' specKey='shaman_ele' category='phase2' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Warlock'>
            <SpecEpPanel name='Affliction (Ruin)' specKey='warlock_affliction_ruin' category='phase2' />
            <SpecEpPanel name='Destruction (Fire)' specKey='warlock_destruction_fire' category='phase2' />
            <SpecEpPanel name='Destruction (Shadow)' specKey='warlock_destruction_shadow' category='phase2' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Warrior'>
            <SpecEpPanel name='Arms' specKey='warrior_arms' category='phase2' />
            <SpecEpPanel name='Fury' specKey='warrior_fury' category='phase2' />
            <SpecEpPanel name='Kebab' specKey='warrior_kebab' category='phase2' />
          </WrapperEpPanel>
        </WrapperEpPanel>
        <WrapperEpPanel name='Phase 1'>
          <WrapperEpPanel name='Hunter'>
            <SpecEpPanel name='Beast Mastery' specKey='hunter_bm' category='phase1' />
            <SpecEpPanel name='Survival' specKey='hunter_surv' category='phase1' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Mage'>
            <SpecEpPanel name='Arcane' specKey='mage_arcane' category='phase1' />
            <SpecEpPanel name='Fire' specKey='mage_fire' category='phase1' />
            <SpecEpPanel name='Frost' specKey='mage_frost' category='phase1' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Priest'>
            <SpecEpPanel name='Shadow' specKey='priest_shadow' category='phase1' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Rogue'>
            <SpecEpPanel name='Assassination' specKey='rogue_assassination' category='phase1' />
            <SpecEpPanel name='Combat' specKey='rogue_combat' category='phase1' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Shaman'>
            <SpecEpPanel name='Enhancement' specKey='shaman_enh' category='phase1' />
            <SpecEpPanel name='Elemental' specKey='shaman_ele' category='phase1' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Warlock'>
            <SpecEpPanel name='Affliction (Ruin)' specKey='warlock_affliction_ruin' category='phase1' />
            <SpecEpPanel name='Destruction (Fire)' specKey='warlock_destruction_fire' category='phase1' />
            <SpecEpPanel name='Destruction (Shadow)' specKey='warlock_destruction_shadow' category='phase1' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Warrior'>
            <SpecEpPanel name='Arms' specKey='warrior_arms' category='phase1' />
            <SpecEpPanel name='Fury' specKey='warrior_fury' category='phase1' />
          </WrapperEpPanel>
        </WrapperEpPanel>
        <WrapperEpPanel name='Pre-raid'>
          <WrapperEpPanel name='Hunter'>
            <SpecEpPanel name='Beast Mastery' specKey='hunter_bm' category='preraid' />
            <SpecEpPanel name='Survival' specKey='hunter_surv' category='preraid' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Mage'>
            <SpecEpPanel name='Arcane' specKey='mage_arcane' category='preraid' />
            <SpecEpPanel name='Fire' specKey='mage_fire' category='preraid' />
            <SpecEpPanel name='Frost' specKey='mage_frost' category='preraid' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Priest'>
            <SpecEpPanel name='Shadow' specKey='priest_shadow' category='preraid' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Rogue'>
            <SpecEpPanel name='Assassination' specKey='rogue_assassination' category='preraid' />
            <SpecEpPanel name='Combat' specKey='rogue_combat' category='preraid' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Shaman'>
            <SpecEpPanel name='Enhancement' specKey='shaman_enh' category='preraid' />
            <SpecEpPanel name='Elemental' specKey='shaman_ele' category='preraid' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Warlock'>
            <SpecEpPanel name='Affliction (Ruin)' specKey='warlock_affliction_ruin' category='preraid' />
            <SpecEpPanel name='Destruction (Fire)' specKey='warlock_destruction_fire' category='preraid' />
            <SpecEpPanel name='Destruction (Shadow)' specKey='warlock_destruction_shadow' category='preraid' />
          </WrapperEpPanel>
          <WrapperEpPanel name='Warrior'>
            <SpecEpPanel name='Arms' specKey='warrior_arms' category='preraid' />
            <SpecEpPanel name='Fury' specKey='warrior_fury' category='preraid' />
          </WrapperEpPanel>
        </WrapperEpPanel>
      </Grid>
    </Content>
  )
}
