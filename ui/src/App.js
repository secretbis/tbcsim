import React, { useReducer } from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from 'react-router-dom';
import _ from 'lodash';
import { Container, Content, Header, Grid, Footer, Row, Col, Button, Panel, Navbar, Nav, Icon, Message } from 'rsuite';

import { initialState, stateReducer } from './state';

import RaidBuffs from './buffs/raid_buffs';
import EquivalencePoints from './ep/equivalence_points';
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
      <p>At this moment, only the following specs are available:</p>
      <ul>
        <li>BM/Survival Hunter</li>
        <li>Enhancement/Elemental Shaman</li>
        <li>Affliction/Destruction Warlock</li>
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
    resourceUsageByAbility: state.resultsResourceUsageByAbility,
    dps: state.resultsDps
  };

  function sim() {
    // TODO: This serialize-deserialize jump can probably be made more efficient
    const config = state.makeSimConfig()
    const simOpts = state.makeSimOptions()

    function cleanKtList(list) {
      let finalList = list
      if(list && list.toArray) {
        finalList = list.toArray()
      }

      // Go one level deeper
      if(finalList.map) {
        finalList = finalList.map(item => {
          if(item && item.toArray) {
            return item.toArray()
          }
          return item
        })
      }

      return finalList
    }

    tbcsim.runSim(config, simOpts,
      ({ opts, iterationsCompleted }) => {
        console.log(`Completed: ${iterationsCompleted}`)
        dispatch({ type: 'iterationsCompleted', value: iterationsCompleted })
      }, (iterations) => {
          const iterList = tbcsim.util.Utils.listWrap(iterations)

          dispatch({ type: 'iterationsCompleted', value: null })
          dispatch({ type: 'iterationResults', value: iterList })

          const resourceUsage = cleanKtList(tbcsim.sim.SimStats.resourceUsage(iterList));
          const resourceUsageByAbility = cleanKtList(tbcsim.sim.SimStats.resourceUsageByAbility(iterList));
          const buffResults = cleanKtList(tbcsim.sim.SimStats.resultsByBuff(iterList));
          const debuffResults = cleanKtList(tbcsim.sim.SimStats.resultsByDebuff(iterList));
          const abilityResults = cleanKtList(tbcsim.sim.SimStats.resultsByAbility(iterList));
          const damageTypeResults = cleanKtList(tbcsim.sim.SimStats.resultsByDamageType(iterList));
          const dps = cleanKtList(tbcsim.sim.SimStats.dps_0(iterList));

          // Compute results
          dispatch({ type: 'resultsResourceUsage', value: resourceUsage })
          dispatch({ type: 'resultsResourceUsageByAbility', value: resourceUsageByAbility })
          dispatch({ type: 'resultsByBuff', value: buffResults })
          dispatch({ type: 'resultsByDebuff', value: debuffResults })
          dispatch({ type: 'resultsByDamageType', value: damageTypeResults })
          dispatch({ type: 'resultsByAbility', value: abilityResults })
          dispatch({ type: 'resultsDps', value: dps })
      }
    )
  }

  function onSimClick() {
    dispatch({ type: 'iterationResults', value: null })
    sim()
  }

  const simDisabled = state.character.class == null || state.iterationsCompleted != null;

  function renderSimulator() {
    return (
      <Content style={{ padding: '20px' }}>
        <Grid fluid={true}>
          <Message type='warning' title={bannerTitle} description={bannerMsg()} />
          <Presets value={state.character} dispatch={dispatch} />
          <Row>
            <Col xs={14} style={{ maxWidth: '750px' }}>
              <Panel header="Gear" bordered>
                {state.character.class ?
                  <GearEditor state={state} character={state.character} dispatch={dispatch}></GearEditor> :
                  <p>Please select a preset above</p>
                }
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
              <Panel header="Sim Options" collapsible bordered defaultExpanded={false}>
                <SimOptions dispatch={dispatch} />
              </Panel>
            </Col>
          </Row>
          <Row style={{ margin: '10px 0 0 0' }}>
            <Button appearance='ghost' disabled={simDisabled} onClick={onSimClick}>Sim!</Button>
            {state.iterationsCompleted != null &&
              <span style={{ marginLeft: '10px' }}>Iterations completed: {state.iterationsCompleted} of {state.simOptions.iterations}</span>
            }
          </Row>
          {state.iterationResults &&
          <Container>
            <SimResults character={state.character} results={resultsData} />
          </Container>
          }
        </Grid>
      </Content>
    )
  }

  // Passing Nav.Item to Link doesn't work as expected, so make something that looks like it but is dumber
  const FakeNavItem = React.forwardRef((props, ref) => (
    <li className='rs-nav-item'>
      <a className='rs-nav-item-content' ref={ref} {...props}>{props.children}</a>
    </li>
  ))

  // App
  return (
    <Router>
      <Container style={{ height: '100%' }}>
        <Header>
          <Navbar>
            <Navbar.Header>
              <h4 style={{ padding: '15px' }}>TBCSim</h4>
            </Navbar.Header>
            <Navbar.Body>
              <Nav>
                <Link to="/">
                  <Nav.Item>Simulator</Nav.Item>
                </Link>
                <Link to="/ep">
                  <Nav.Item>Equivalence Points</Nav.Item>
                </Link>
              </Nav>
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
        <Switch>
          <Route exact path="/">
            {renderSimulator()}
          </Route>
          <Route path="/ep">
            <EquivalencePoints />
          </Route>
        </Switch>
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
    </Router>
  );
}

export default App;
