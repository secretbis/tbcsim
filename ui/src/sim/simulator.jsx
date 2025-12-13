import React, { useReducer } from 'react';

import _ from 'lodash';
import { Container, Content, CustomProvider, Header, Grid, Footer, Row, Col, Button, Panel, Message } from 'rsuite';

import CombatLog from '../combat_log/combat_log';
import GearEditor from '../gear/gear_editor';
import Presets from '../presets/presets';
import RaidBuffs from '../buffs/raid_buffs';
import Rotation from '../rotation/rotation';
import SimOptions from '../sim/options';
import SimResults from '../results/results';
import Talents from '../talents/talents';

import { StateProvider, useDispatchContext, useStateContext } from '../state';

import * as tbcsim from 'tbcsim';

const bannerTitle = 'Hello!  This is a work in progress.'
function bannerMsg() {
  return (
    <div>
      <p>At this moment, the following specs are not implemented:</p>
      <ul>
        <li>Ret Paladin</li>
        <li>Balance/Feral Druid</li>
      </ul>
    </div>
  );
}

export default function() {
  const state = useStateContext();
  const dispatch = useDispatchContext();

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
          const iterList = tbcsim.Utils.getInstance().listWrap(iterations)

          dispatch({ type: 'iterationsCompleted', value: null })
          dispatch({ type: 'iterationResults', value: iterList })

          const simStats = tbcsim.SimStats.getInstance()
          const resourceUsage = cleanKtList(simStats.resourceUsage(iterList));
          const resourceUsageByAbility = cleanKtList(simStats.resourceUsageByAbility(iterList));
          const buffResults = cleanKtList(simStats.resultsByBuff(iterList));
          const debuffResults = cleanKtList(simStats.resultsByDebuff(iterList));
          const abilityResults = cleanKtList(simStats.resultsByAbility(iterList));
          const damageTypeResults = cleanKtList(simStats.resultsByDamageType(iterList));
          const dps = cleanKtList(simStats.dps(iterList));

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
    dispatch({ type: 'resultsResourceUsageByAbility', value: null })
    dispatch({ type: 'resultsByBuff', value: null })
    dispatch({ type: 'resultsByDebuff', value: null })
    dispatch({ type: 'resultsByDamageType', value: null })
    dispatch({ type: 'resultsByAbility', value: null })
    dispatch({ type: 'resultsDps', value: null })
    sim()
  }

  const simDisabled = state.character.class == null || state.iterationsCompleted != null;

  return (
    <Content>
      <Grid fluid={true}>
        <Message type='warning' header={bannerTitle} closable>{bannerMsg()}</Message>
        <Presets />
        <Row>
          <Col xs={14} style={{ maxWidth: '750px' }}>
            <Panel header="Gear" bordered>
              {state.character.class ? <GearEditor /> : <p>Please select a preset above</p>}
            </Panel>
          </Col>
          <Col xs={10}>
            <Panel header="Buffs" collapsible bordered shaded defaultExpanded={false}>
              <RaidBuffs stateKey='raidBuffs' />
            </Panel>
            <Panel header="Debuffs" collapsible bordered defaultExpanded={false}>
              <RaidBuffs stateKey='raidDebuffs' />
            </Panel>
            <Panel header="Rotation" collapsible bordered defaultExpanded={false}>
              <Rotation />
            </Panel>
            <Panel header="Talents" collapsible bordered defaultExpanded={false}>
              <Talents />
            </Panel>
            <Panel header="Sim Options" collapsible bordered defaultExpanded={false}>
              <SimOptions />
            </Panel>
          </Col>
        </Row>
        <Row style={{ margin: '10px 0 0 0' }}>
          <Button appearance='ghost' disabled={simDisabled} onClick={onSimClick}>Sim!</Button>
          {state.iterationsCompleted == null &&
            <CombatLog iterations={state.iterationResults} />
          }
          {state.iterationsCompleted != null &&
            <span style={{ marginLeft: '10px' }}>Iterations completed: {state.iterationsCompleted} of {state.simOptions.iterations}</span>
          }
        </Row>
        {state.iterationResults &&
        <Container>
          <SimResults results={resultsData} />
        </Container>
        }
      </Grid>
    </Content>
  )
}
