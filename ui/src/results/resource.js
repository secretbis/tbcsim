import React from 'react';
import _ from 'lodash';

import { ResponsiveLine } from '@nivo/line'
import { Container } from 'rsuite'

import { linkedHashMapKeys } from '../util/util';

const ResourceLineChart = ({ character, data, config }) => (
  <ResponsiveLine
      theme={{
        textColor: '#e9ebf0'
      }}
      data={data}
      margin={{ top: 50, right: 110, bottom: 50, left: 60 }}
      xScale={{ type: 'linear' }}
      yScale={{ type: 'linear', min: 0, max: config.yMax || 100, stacked: true, reverse: false }}
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
          legend: config.title,
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
      colors={config.lineColor}
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
              symbolBorderColor: config.lineColor,
              effects: [
                  {
                      on: 'hover',
                      style: {
                          itemBackground: config.lineColor,
                          itemOpacity: 1
                      }
                  }
              ]
          }
      ]}
  />
)

// TODO: Druid specs
function chartConfig(character, resourceType) {
  const cls = (character.class || 'hunterpet').toLowerCase()
  if(cls == 'warrior') {
    return {
      title: 'Rage',
      lineColor: '#FF0000'
    };
  } else if(cls == 'rogue') {
    if(resourceType == 'ENERGY') {
      return {
        title: 'Energy',
        lineColor: '#FFFF00'
      };
    } else {
      return {
        title: 'Combo Points',
        lineColor: '#FF0000',
        yMax: 5
      };
    }
  } else if(cls == 'hunterpet') {
    return {
      title: 'Focus',
      lineColor: '#FFA500'
    };
  } else {
    return {
      title: 'Mana',
      lineColor: '#0000FF'
    }
  }
}

export default function({ character, data }) {
  return (
    <Container>
      {linkedHashMapKeys(data).map(key => {
        const resourceData = data.get_35(key)
        if(resourceData == null) return null;

        const config = chartConfig(character, key)

        // Transform the data how the chart lib wants it
        // We only have one resource line
        const actualData = [{
          id: config.title,
          data: _.uniqBy(
            // If there are collisions, choose the larger value
            _.orderBy(
              resourceData.series.toArray().map(d => ({
                  x: d._first,
                  // Combo points are charted by amount, not by amountPct
                  y: key == 'COMBO_POINT' ? d._second / 20 : d._second
                })
              ), ['x', 'y'], ['asc', 'desc']
            ), 'x'
          )
        }]

        return (
          <Container key={key}>
            <h5>Resource Breakdown ({key})</h5>
            <Container style={{ height: '400px' }}>
              <ResourceLineChart character={character} data={actualData} config={config} />
            </Container>
          </Container>
        )
      })}
    </Container>
  )
}
