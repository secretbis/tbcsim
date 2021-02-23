import React, { useReducer, useState } from 'react';
import { Container, Content, Header, Grid, Footer, Row, Button, Panel, Navbar, Nav, Icon, Message } from 'rsuite';

import simDefaults from './data/sim_defaults';
import GearEditor from './gear/gear_editor';
import Presets from './presets/presets';
import SimOptions from './sim/options';
import SimResults from './results/results';

import * as tbcsim from 'tbcsim';

import './App.css';

function stateReducer(state, action) {
  let newState = state
  if (state.hasOwnProperty(action.type)) {
    newState = { ...state, [action.type]: state[action.type] = action.value };
  } else {
    console.warn(`Unhandled action type: ${action.type}`);
  }

  // Compute some props
  if(newState.durationSeconds != null) {
    newState.durationMs = newState.durationSeconds * 1000
  }

  if(newState.durationVariabilitySeconds != null) {
    newState.durationVariabilityMs = newState.durationVariabilitySeconds * 1000
  }

  return newState
}

const initialState = {
  iterationsCompleted: null,
  iterationResults: null,

  resultsByAbility: null,
  resultsByBuff: null,
  resultsByDebuff: null,
  resultsByDamageType: null,
  resultsResourceUsage: null,
  resultsDps: null,

  durationSeconds: simDefaults.durationSeconds,
  durationVariabilitySeconds: simDefaults.durationVariabilitySeconds,
  stepMs: simDefaults.stepMs,
  latencyMs: simDefaults.latencyMs,
  iterations: simDefaults.iterations,
  targetLevel: simDefaults.targetLevel,
  targetArmor: simDefaults.targetArmor,
  allowParryAndBlock: simDefaults.allowParryAndBlock,
  showHiddenBuffs: simDefaults.showHiddenBuffs,

  character: null,
};

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
    const config = tbcsim.sim.config.ConfigMaker.fromJson(JSON.stringify(state.character))

    const simOpts = new tbcsim.sim.SimOptions(
      state.durationMs,
      state.durationVariabilityMs,
      state.stepMs,
      state.latencyMs,
      state.iterations,
      state.targetLevel,
      state.targetArmor,
      state.allowParryAndBlock,
      state.showHiddenBuffs
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

  const simDisabled = state.character == null || state.iterationsCompleted != null;

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
          <Container style={{padding: '10px 0px', fontWeight: 800}}>
            <Presets value={state.character} dispatch={dispatch} />
          </Container>
          <Container>
            <GearEditor character={state.character}></GearEditor>
          </Container>
          <Container style={{maxWidth: '700px'}}>
            <Panel header="Sim Options" collapsible bordered defaultExpanded={false}>
              <SimOptions dispatch={dispatch} />
            </Panel>
          </Container>
          <Row>
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
