import React from 'react';
import _ from 'lodash';

import { toFixed, toFixedPct } from './formatters';
import Table from './table';

const columnInfo = [
  {
    title: 'Name',
    key: 'name',
    flex: 2,
    minWidth: 200
  },{
    title: 'CountAvg',
    key: 'countAvg',
    formatter: toFixed()
  },{
    title: 'ThreatPerSecAvg',
    key: 'threatPerSecondAvg',
    formatter: toFixed()
  },{
    title: 'ThreatPerCastAvg',
    key: 'threatPerCastAvg',
    formatter: toFixed()
  }
]

export default (props) => {
  const totalTps = toFixed()(_.sumBy(props.data, 'threatPerSecondAvg'));
  return (
    <Table
      title={'Ability Threat Breakdown'}
      subtitle={`Total TPS: ${totalTps}`}
      data={props.data}
      columnInfo={columnInfo}
    />
  )
}
