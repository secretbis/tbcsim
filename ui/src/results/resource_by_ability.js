import React from 'react';
import { Container } from 'rsuite'

import { toFixed } from './formatters';
import Table from './table';

import { linkedHashMapKeys } from '../util/util';

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
  return linkedHashMapKeys(props.data).map(key => {
    const data = props.data.get_35(key)
    if(data == null) return null;

    return (
      <Container style={{ marginBottom: '20px' }}>
        <Table
          key={key}
          title={`Resource Gain/Usage By Ability (${key})`}
          data={data._array_3}
          columnInfo={columnInfo}
        />
      </Container>
    )
  })
}
