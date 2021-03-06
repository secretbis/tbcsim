import React, { useReducer, useState } from 'react';
import _ from 'lodash';
import { Container, Content, Header, Grid, Footer, Row, Col, Button, Panel, Navbar, Nav, Icon, Message } from 'rsuite';

import { initialState, stateReducer } from './state';

import RaidBuffs from './buffs/raid_buffs';
import GearEditor from './gear/gear_editor';
import Presets from './presets/presets';
import SimResults from './results/results';
import Rotation from './rotation/rotation';
import SimOptions from './sim/options';
import Talents from './talents/talents';

import * as tbcsim from 'tbcsim';

import './App.css';

const bannerTitle = 'Hello!  This is a work in progress.'
function bannerMsg() {
  return (
    <div>
      <p>At this moment, only a few specs are available:</p>
      <ul>
        <li>Enhancement/Elemental Shaman</li>
        <li>Arms/Fury Warrior</li>
      </ul>
    </div>
  );
}

function App() {
  const [state, dispatch] = useReducer(stateReducer, initialState)
  const [gearExpanded, setGearExpanded] = useState(true);

  const resultsData = {
    ability: state.resultsByAbility,
    buff: state.resultsByBuff,
    debuff: state.resultsByDebuff,
    damageType: state.resultsByDamageType,
    resourceUsage: state.resultsResourceUsage,
    dps: state.resultsDps
  };

  function sim() {
    // TODO: This serialize-deserialize jump can probably be made more efficient
    const config = tbcsim.sim.config.ConfigMaker.fromJson(JSON.stringify({
      class: state.character.class,
      description: state.character.description,
      spec: state.character.spec,
      race: state.character.race,
      level: state.character.level,
      gear: _.mapValues(state.character.gear, it => ({
        name: it.name,
        gems: it.sockets ? it.sockets.map(sk => sk && sk.gem.name) : [],
        enchant: it.enchant ? it.enchant.displayName : null
      })),
      rotation: state.character.rotation,
      talents: state.character.talents,

      raidBuffs: _.keys(_.pickBy(state.raidBuffs, value => !!value)),
      raidDebuffs: _.keys(_.pickBy(state.raidDebuffs, value => !!value))
    }))

    const simOpts = new tbcsim.sim.SimOptions(
      state.simOptions.durationSeconds * 1000,
      state.simOptions.durationVariabilitySeconds * 1000,
      state.simOptions.stepMs,
      state.simOptions.latencyMs,
      state.simOptions.iterations,
      state.simOptions.targetLevel,
      state.simOptions.targetArmor,
      state.simOptions.allowParryAndBlock,
      state.simOptions.showHiddenBuffs
    )

    function cleanKtList(list) {
      let finalList = list
      if(list && list.toArray) {
        return list.toArray()
      }

      return []
    }

    tbcsim.runSim(config, simOpts,
      ({ opts, iterationsCompleted }) => {
        console.log(`Completed: ${iterationsCompleted}`)
        dispatch({ type: 'iterationsCompleted', value: iterationsCompleted })
      }, (iterations) => {
          const iterList = tbcsim.util.Utils.listWrap(iterations)

          dispatch({ type: 'iterationsCompleted', value: null })
          dispatch({ type: 'iterationResults', value: iterList })

          const buffResults = cleanKtList(tbcsim.sim.SimStats.resultsByBuff(iterList));
          const debuffResults = cleanKtList(tbcsim.sim.SimStats.resultsByDebuff(iterList));
          const abilityResults = cleanKtList(tbcsim.sim.SimStats.resultsByAbility(iterList));
          const damageTypeResults = cleanKtList(tbcsim.sim.SimStats.resultsByDamageType(iterList));

          // Compute results
          dispatch({ type: 'resultsResourceUsage', value: tbcsim.sim.SimStats.resourceUsage(iterList) })
          dispatch({ type: 'resultsByBuff', value: buffResults })
          dispatch({ type: 'resultsByDebuff', value: debuffResults })
          dispatch({ type: 'resultsByDamageType', value: damageTypeResults })
          dispatch({ type: 'resultsByAbility', value: abilityResults })
          dispatch({ type: 'resultsDps', value: tbcsim.sim.SimStats.dps_0(iterList) })
      }
    )
  }

  function onSimClick() {
    dispatch({ type: 'iterationResults', value: null })
    sim()
  }

  const simDisabled = state.character.class == null || state.iterationsCompleted != null;

  // App
  return (
    <Container style={{ height: '100%' }}>
      <Header>
        <Navbar>
          <Navbar.Header>
            <h4 style={{ padding: '15px' }}>TBCSim</h4>
          </Navbar.Header>
          <Navbar.Body>
            <Nav pullRight>
              <Nav.Item
                icon={<Icon icon='github' />}
                style={{ color: '#e9ebf0', textDecoration: 'none' }}
                href='https://github.com/marisa-ashkandi/tbcsim/issues/new'
                target='_blank'
                rel='noreferrer noopener'>Report a Bug
              </Nav.Item>
            </Nav>
          </Navbar.Body>
        </Navbar>
      </Header>
      <Content style={{ padding: '20px' }}>
        <Grid fluid={true}>
          <Message type='warning' title={bannerTitle} description={bannerMsg()} />
          <Presets value={state.character} dispatch={dispatch} />
          <Row style={{maxWidth: '1400px'}}>
            <Col xs={14}>
              <Panel header="Gear" collapsible bordered onClick={() => setGearExpanded(!gearExpanded)} expanded={gearExpanded && state.character.class !== null}>
                <GearEditor character={state.character} dispatch={dispatch}></GearEditor>
              </Panel>
            </Col>
            <Col xs={10}>
              <Panel header="Buffs" collapsible bordered defaultExpanded={false}>
                <RaidBuffs state={state.raidBuffs} stateKey='raidBuffs' dispatch={dispatch} />
              </Panel>
              <Panel header="Debuffs" collapsible bordered defaultExpanded={false}>
                <RaidBuffs state={state.raidDebuffs} stateKey='raidDebuffs' dispatch={dispatch} />
              </Panel>
              <Panel header="Rotation" collapsible bordered defaultExpanded={false}>
                <Rotation rotation={state.character.rotation} dispatch={dispatch} />
              </Panel>
              <Panel header="Talents" collapsible bordered defaultExpanded={false}>
                <Talents talents={state.character.talents} dispatch={dispatch} />
              </Panel>
            </Col>
          </Row>
          <Panel header="Sim Options" collapsible bordered defaultExpanded={false} style={{maxWidth: '700px'}}>
            <SimOptions dispatch={dispatch} />
          </Panel>
          <Row style={{ margin: '10px 0 0 0' }}>
            <Button appearance='ghost' disabled={simDisabled} onClick={onSimClick}>Sim!</Button>
            {state.iterationsCompleted != null &&
              <span style={{ marginLeft: '10px' }}>Iterations completed: {state.iterationsCompleted}</span>
            }
          </Row>
          {state.iterationResults &&
          <Container>
            <SimResults character={state.character} results={resultsData} />
          </Container>
          }
        </Grid>
      </Content>
      <Footer>
        <Navbar className='justify-content-center'>
          <Navbar.Body>
            <Nav>
              <Nav.Item></Nav.Item>
              Footer KEKW
            </Nav>
          </Navbar.Body>
        </Navbar>
      </Footer>
    </Container>
  );
}

export default App;
