import React, { useState } from 'react';
import { Container, Content, Header, Grid, Footer, Row, Button, Navbar, Nav, Icon, Message } from 'rsuite';

import simDefaults from './data/simdefaults';
import Presets from './presets/presets';
import SimOptions from './sim/options';
import * as tbcsim from 'tbcsim';

import './App.css';

const bannerTitle = "Hello!  This is a work in progress."
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
  // Sim status
  const [iterationsCompleted, setIterationsCompleted] = useState(null);

  // Sim options state
  const [durationSeconds, setDurationSeconds] = useState(simDefaults.durationSeconds);
  const [durationVariabilitySeconds, setDurationVariabilitySeconds] = useState(simDefaults.durationVariabilitySeconds);
  const [stepMs, setStepMs] = useState(simDefaults.stepMs);
  const [latencyMs, setLatencyMs] = useState(simDefaults.latencyMs);
  const [iterations, setIterations] = useState(simDefaults.iterations);
  const [targetLevel, setTargetLevel] = useState(simDefaults.targetLevel);
  const [targetArmor, setTargetArmor] = useState(simDefaults.targetArmor);
  const [allowParryAndBlock, setAllowParryAndBlock] = useState(simDefaults.allowParryAndBlock);
  const [showHiddenBuffs, setShowHiddenBuffs] = useState(simDefaults.showHiddenBuffs);

  const optSetters = {
    durationSeconds: setDurationSeconds,
    durationVariabilitySeconds: setDurationVariabilitySeconds,
    stepMs: setStepMs,
    latencyMs: setLatencyMs,
    iterations: setIterations,
    targetLevel: setTargetLevel,
    targetArmor: setTargetArmor,
    allowParryAndBlock: setAllowParryAndBlock,
    showHiddenBuffs: setShowHiddenBuffs
  }

  const optData = {
    durationMs: durationSeconds * 1000,
    durationVariabilityMs: durationVariabilitySeconds * 1000,
    stepMs,
    latencyMs,
    iterations,
    targetLevel,
    targetArmor,
    allowParryAndBlock,
    showHiddenBuffs,
  };

  // Character/preset state
  const [characterPreset, setCharacterPreset] = React.useState();

  function sim(rawOpts, preset) {
    // TODO: This serialize-deserialize jump can probably be made more efficient
    const config = tbcsim.sim.config.ConfigMaker.fromJson(JSON.stringify(preset))

    const simOpts = new tbcsim.sim.SimOptions(
      rawOpts.durationMs,
      rawOpts.durationVariabilityMs,
      rawOpts.stepMs,
      rawOpts.latencyMs,
      rawOpts.iterations,
      rawOpts.targetLevel,
      rawOpts.targetArmor,
      rawOpts.allowParryAndBlock,
      rawOpts.showHiddenBuffs
    )

    tbcsim.runSim(config, simOpts,
      ({ opts, iterationsCompleted }) => {
        console.log(`Completed: ${iterationsCompleted}`)
        setIterationsCompleted(iterationsCompleted)
      }, (iterations) => {
          setIterationsCompleted(null)
          debugger
      }
    )
  }

  function onSimClick() {
    sim(optData, characterPreset)
  }

  const simDisabled = characterPreset == null || iterationsCompleted != null;

  // App
  return (
    <Container style={{ height: "100%" }}>
      <Header>
        <Navbar>
          <Navbar.Header>
            <h3 style={{padding: '10px 15px'}}>TBCSim</h3>
          </Navbar.Header>
          <Navbar.Body>
            <Nav pullRight>
              <Nav.Item icon={<Icon icon='github' />}>
                <a style={{color: '#e9ebf0', textDecoration: 'none'}} href='https://github.com/marisa-ashkandi/tbcsim/issues/new'>Report a Bug</a>
              </Nav.Item>
            </Nav>
          </Navbar.Body>
        </Navbar>
      </Header>
      <Content style={{ padding: '20px' }}>
        <Grid fluid={true}>
          <Message type="warning" title={bannerTitle} description={bannerMsg()} />
          <Container style={{padding: '10px 0px', fontWeight: 800}}>
            <Presets value={characterPreset} setter={setCharacterPreset} />
          </Container>
          <Container style={{padding: '10px 0px', maxWidth: '700px'}}>
            <SimOptions setters={optSetters} />
          </Container>
          <Row>
            <Button disabled={simDisabled} onClick={onSimClick}>Sim!</Button>
            {iterationsCompleted != null &&
              <span style={{ marginLeft: "10px" }}>Iterations completed: {iterationsCompleted}</span>
            }
          </Row>
        </Grid>
      </Content>
      <Footer>
        <Navbar className="justify-content-center">
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
