import React from 'react';
import _ from 'lodash';

import { ResponsiveBar } from '@nivo/bar'
import { Container, Content, Grid, Row, Col, Panel, Message } from 'rsuite'

import rankingData from './data/ranks_all.json'

const bannerTitle = 'Rankings are very beta!'
function bannerMsg() {
  return (
    <div>
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
          <li>Rankings are per-tier.  Each preset has a tier-specific BIS or BIS-adjacent gear set (if it isn't BIS/BIS-adjacent, please file a bug!)</li>
        </ul>
        <h5>Notes and Caveats</h5>
        <ul>
          <li>Each class is simulated in "ideal" conditions.  For example, supporting 10 Rogues/Fury Warriors with perfect melee groups is not possible - a 25-man raid would max out at approximately two.</li>
          <li>If you are interested in how these classes perform in different conditions, head over to the Simulator tab and subtract some buffs!</li>
        </ul>
      </Row>
    </Container>
  );
}

const RankingBarChart = ({ data }) => (
  <ResponsiveBar
    data={data}
    indexBy='spec_display'
    margin={{ top: 50, right: 0, bottom: 0, left: 200 }}
    colors={node => {
      const specColor = _.get(specColors, node.data.spec, '#000000')
      const specPetColor = _.get(specPetColors, node.data.spec, specColor);
      return node.id === 'subjectPetMean' ? specPetColor : specColor;
    }}
    layout='horizontal'
    valueScale={{ type: 'linear' }}
    indexScale={{ type: 'band', round: true }}
    keys={['subjectMean', 'subjectPetMean']}
    padding={0.2}
    labelTextColor='inherit:darker(1.6)'
    labelSkipWidth={16}
    labelSkipHeight={16}
    enableGridX={true}
    enableGridY={false}
    label={d => d && d.value != null ? d.value.toFixed(2) : '' }
    axisTop={{
        tickSize: 5,
        tickPadding: 5,
        tickRotation: 0,
        legend: 'DPS (3 minutes)',
        itemTextColor: '#fff',
        legendPosition: 'middle',
        legendOffset: -40
    }}
    tooltip={(props) => (
        <strong style={{ }}>
            Total: {props.data.totalMean.toFixed(2)}
        </strong>
    )}
    theme={{
      axis: {
        ticks: {
          line: {
            stroke: '#777'
          },
          text: {
            fill: '#fff',
            fontSize: '14px',
          }
        },
        legend: {
          text: {
            fill: '#fff',
            fontSize: '14px',
          }
        }
      },
      labels: {
        text: {
          fontSize: '14px'
        }
      },
      grid: {
        line: {
          stroke: '#777'
        }
      },
      tooltip: {
        container: {
          background: '#333',
        },
      },
    }}
    animate={true}
    motionStiffness={90}
    motionDamping={15}
  />
)

const specDisplayNames = {
  hunter_bm: 'Beast Mastery Hunter',
  hunter_surv: 'Survival Hunter',
  shaman_ele: 'Elemental Shaman',
  shaman_enh: 'Enhancement Shaman',
  warlock_affliction_ruin: 'Affliction Warlock (Ruin)',
  warlock_affliction_ua: 'Affliction Warlock (UA)',
  warlock_destruction_fire: 'Destruction Warlock (Fire)',
  warlock_destruction_shadow: 'Destruction Warlock (Shadow)',
  warrior_arms: 'Arms Warrior',
  warrior_fury: 'Fury Warrior',
}

// Other colors for later:
// Druid: #FF7C0A
// Mage: #3FC7EB
// Paladin: #F48CBA
// Priest: #FFFFFF
// Rogue: #FFF468
const specColors = {
  hunter_bm: '#AAD372',
  hunter_surv: '#AAD372',
  shaman_ele: '#0070DD',
  shaman_enh: '#0070DD',
  warlock_affliction_ruin: '#8788EE',
  warlock_affliction_ua: '#8788EE',
  warlock_destruction_fire: '#8788EE',
  warlock_destruction_shadow: '#8788EE',
  warrior_arms: '#C69B6D',
  warrior_fury: '#C69B6D',
}

const specPetColors = {
  hunter_bm: '#64BDC8',
  hunter_surv: '#64BDC8'
}

function SpecRankingPanel({ name, category, collapsible=true }) {
  const categoryData = _.get(rankingData, category, {})
  const categoryRankings = _.sortBy(Object.keys(categoryData).map(key => {
    const rankingData = categoryData[key];

    return {
      spec: key,
      spec_display: _.get(specDisplayNames, key, key),
      ...rankingData
    }
  }), 'totalMean')

  return (
    <Row>
      <Col xs={24}>
        <Panel
          collapsible={collapsible}
          defaultExpanded={true}
          header={name}
          bordered
          style={{ width: '100%' }}
        >
          <div style={{ width: '100%', height: 500 }}>
            <RankingBarChart data={categoryRankings} />
          </div>
        </Panel>
      </Col>
    </Row>
  )
}

export default function() {
  return (
    <Content style={{ padding: '20px' }}>
      <Grid fluid={true}>
        <Message type='warning' title={bannerTitle} description={bannerMsg()} />
        <HowItWorks />
        <SpecRankingPanel name='Pre-raid' category='preraid' />
      </Grid>
    </Content>
  )
}
