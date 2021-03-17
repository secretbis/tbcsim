import React from 'react';

import { toFixed } from './formatters';
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
    title: 'TotalGainAvg',
    key: 'totalGainAvg',
    formatter: toFixed()
  },{
    title: 'GainPerCountAvg',
    key: 'gainPerCountAvg',
    formatter: toFixed()
  }
]

export default (props) => {
  return (
    <Table
      title={'Resource Gain/Usage By Ability'}
      data={props.data}
      columnInfo={columnInfo}
    />
  )
}
