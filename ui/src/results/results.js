import React from 'react';
import { Container, Col, Row } from 'rsuite';

import AbilityResults from './ability';
import BuffResults from './buffs';
import DamageTypeResults from './damagetype';
import ResourceUsage from './resource';

import { toFixed } from './formatters';

export default function({ character, results }) {
  const { ability, buff, debuff, damageType, resourceUsage, dps } = results

  if(!ability || !buff || !debuff || !damageType || !resourceUsage || !dps) {
    return null;
  }

  return (
    <Container style={{ marginTop: '20px', marginBottom: '20px' }}>
      <Row>
        <h4><b>AVERAGE DPS: {toFixed()(dps.mean)}</b></h4>
        <p>MEDIAN DPS: {toFixed()(dps.median)}</p>
        <p>STDDEV DPS: {toFixed()(dps.sd)}</p>
      </Row>
      <Row style={{ marginTop: '20px', marginBottom: '20px' }}>
        <Col>
          <AbilityResults data={ability} />
        </Col>
      </Row>
      <Row>
        <Col xs={12}>
          <BuffResults title={'Buff Breakdown'} data={buff} />
        </Col>
        <Col xs={12}>
          <BuffResults title={'Debuff Breakdown'} data={debuff} />
          <Container style={{ marginTop: '20px', marginBottom: '20px' }}>
            <DamageTypeResults data={damageType} />
          </Container>
          <ResourceUsage character={character} data={resourceUsage} />
        </Col>
      </Row>
    </Container>
  )
}
