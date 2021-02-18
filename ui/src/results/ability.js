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
  },{
    title: 'AvgHit',
    key: 'avgHit',
    formatter: toFixed()
  },{
    title: 'AvgCrit',
    key: 'avgCrit',
    formatter: toFixed()
  },{
    title: 'Hit%',
    key: 'hitPct',
    formatter: toFixedPct()
  },{
    title: 'Crit%',
    key: 'critPct',
    formatter: toFixedPct()
  },{
    title: 'Miss%',
    key: 'missPct',
    formatter: toFixedPct()
  },{
    title: 'Dodge%',
    key: 'dodgePct',
    formatter: toFixedPct()
  },{
    title: 'Parry%',
    key: 'parryPct',
    formatter: toFixedPct()
  },{
    title: 'Glance%',
    key: 'glancePct',
    formatter: toFixedPct()
  }
]

export default (props) => {
  return (
    <Table
      title={'Ability Breakdown'}
      data={props.data}
      columnInfo={columnInfo}
    />
  )
}
