import React from 'react';

import Table from './table';

const columnInfo = [
  {
    title: "Name",
    flex: 2,
    key: "name"
  },{
    title: "AppliedCountAvg",
    flex: 1,
    key: "appliedAvg"
  },{
    title: "RefreshedCountAvg",
    flex: 1,
    key: "refreshedAvg"
  },{
    title: "UptimePct",
    flex: 1,
    key: "uptimePct"
  },{
    title: "AvgDurationSeconds",
    flex: 1,
    key: "avgDuration"
  },{
    title: "AvgStacks",
    flex: 1,
    key: "avgStacks"
  }
];

export default (props) => {
  return (
    <Table
      data={props.data}
      columnInfo={columnInfo}
    />
  )
}
