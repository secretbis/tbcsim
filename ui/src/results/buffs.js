import React from 'react';

import { toFixed, toFixedPct } from './formatters';
import Table from './table';

const columnInfo = [
  {
    title: 'Name',
    key: 'name',
    flex: 2,
    minWidth: 200
  },{
    title: 'AppliedCountAvg',
    key: 'appliedAvg',
    formatter: toFixed()
  },{
    title: 'RefreshedCountAvg',
    key: 'refreshedAvg',
    formatter: toFixed()
  },{
    title: 'UptimePct',
    key: 'uptimePct',
    formatter: toFixedPct()
  },{
    title: 'AvgDurationSeconds',
    key: 'avgDuration',
    formatter: toFixed()
  },{
    title: 'AvgStacks',
    key: 'avgStacks',
    formatter: toFixed()
  }
];

export default ({ title, data }) => {
  return (
    <Table
      title={title}
      data={data}
      columnInfo={columnInfo}
    />
  )
}
