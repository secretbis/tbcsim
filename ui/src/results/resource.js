import React from 'react';
import _ from 'lodash';

import { ResponsiveLine } from '@nivo/line'
import { Container } from 'rsuite'

import { kprop } from '../util/util';

const ResourceLineChart = ({ character, data }) => (
  <ResponsiveLine
      theme={{
        textColor: '#e9ebf0'
      }}
      data={data}
      margin={{ top: 50, right: 110, bottom: 50, left: 60 }}
      xScale={{ type: 'linear' }}
      yScale={{ type: 'linear', min: 0, max: 100, stacked: true, reverse: false }}
      yFormat=" >-.2f"
      axisTop={null}
      axisRight={null}
      axisBottom={{
          orient: 'bottom',
          tickSize: 5,
          tickPadding: 5,
          tickRotation: 0,
          legend: 'Time (s)',
          legendOffset: 36,
          legendPosition: 'middle'
      }}
      axisLeft={{
          orient: 'left',
          tickSize: 5,
          tickPadding: 5,
          tickRotation: 0,
          legend: resourceTypeForClass(character),
          legendOffset: -40,
          legendPosition: 'middle'
      }}
      tooltip={({ point }) => {
        const isMana = point.serieId === 'Mana';
        let value = isMana ? point.data.y.toFixed(2) : point.data.y;
        const suffix = isMana ? '%' : '';

        return (
          <strong style={{ color: 'white' }}>
            {point.serieId}: {value}{suffix}
          </strong>
        )
      }}
      enablePoints={false}
      enableGridX={false}
      colors={lineColorForClass(character)}
      useMesh={true}
      legends={[
          {
              anchor: 'bottom-right',
              direction: 'column',
              justify: false,
              translateX: 100,
              translateY: 0,
              itemsSpacing: 0,
              itemDirection: 'left-to-right',
              itemWidth: 80,
              itemHeight: 20,
              itemOpacity: 0.75,
              symbolSize: 12,
              symbolShape: 'circle',
              symbolBorderColor: lineColorForClass(character),
              effects: [
                  {
                      on: 'hover',
                      style: {
                          itemBackground: lineColorForClass(character),
                          itemOpacity: 1
                      }
                  }
              ]
          }
      ]}
  />
)

// TODO: Druid specs
function resourceTypeForClass(character) {
  const cls = (character.class || 'hunterpet').toLowerCase()
  if(cls == 'warrior') {
    return 'Rage';
  } else if(cls == 'rogue') {
    return 'Energy';
  } else if(cls == 'hunterpet') {
    return 'Focus';
  } else {
    return 'Mana'
  }
}

function lineColorForClass(character) {
  const cls = (character.class || 'hunterpet').toLowerCase()
  if(cls == 'warrior') {
    return '#FF0000';
  } else if(cls == 'rogue') {
    return '#FFFF00';
  } else if(cls == 'hunterpet') {
    return '#FFA500';
  } else {
    return '#0000FF'
  }
}

export default function({ character, data }) {
  // Transform the data how the chart lib wants it
  // We only have one resource line
  const actualData = [{
    id: resourceTypeForClass(character),
    data: _.uniqBy(data.series.toArray().map(d => ({
      x: kprop(d, 'first'),
      y: kprop(d, 'second')
    })), 'x')
  }]

  return (
    <Container>
      <h5>Resource Breakdown</h5>
      <Container style={{ height: '400px' }}>
        <ResourceLineChart character={character} data={actualData} />
      </Container>
    </Container>
  )
}
