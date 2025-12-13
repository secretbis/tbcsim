import React from 'react';

import { toFixed, toFixedPct } from './formatters';
import Table from './table';

const columnInfo = [
  {
    title: 'Type',
    key: 'type',
    flex: 2,
    minWidth: 150
  },{
    title: 'CountAvg',
    key: 'countAvg',
    formatter: toFixed()
  },{
    title: 'TotalDmgAvg',
    key: 'totalAvg',
    formatter: toFixed()
  },{
    title: 'PctOfTotal',
    key: 'pctOfTotal',
    formatter: toFixedPct()
  }
]

export default ({data}) => {
  return (
    <Table
      title='Damage Type Breakdown'
      data={data}
      columnInfo={columnInfo}
      icons={false}
    />
  )
}
