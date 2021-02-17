import React from 'react';

import Table from './table';

const columnInfo = [
  {
    title: "Name",
    flex: 1,
    key: "name"
  },{
    title: "CountAvg",
    flex: 1,
    key: "countAvg"
  },{
    title: "TotalDmgAvg",
    flex: 1,
    key: "totalAvg"
  },{
    title: "PctOfTotal",
    flex: 1,
    key: "pctOfTotal"
  },{
    title: "AvgHit",
    flex: 1,
    key: "avgHit"
  },{
    title: "AvgCrit",
    flex: 1,
    key: "avgCrit"
  },{
    title: "Hit%",
    flex: 1,
    key: "hitPct"
  },{
    title: "Crit%",
    flex: 1,
    key: "critPct"
  },{
    title: "Miss%",
    flex: 1,
    key: "missPct"
  },{
    title: "Dodge%",
    flex: 1,
    key: "dodgePct"
  },{
    title: "Parry%",
    flex: 1,
    key: "parryPct"
  },{
    title: "Glance%",
    flex: 1,
    key: "glancePct"
  }
]

export default (props) => {
  return (
    <Table
      data={props.data}
      columnInfo={columnInfo}
    />
  )
}
