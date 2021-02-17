import React from 'react';
import { Table } from 'rsuite';

const { Column, HeaderCell, Cell } = Table;

export default (props) => {
  return (
    <Table
      autoHeight={true}
      data={props.data}
    >
      {props.columnInfo.map(col => {
        return (
          <Column flexGrow={col.style}>
            <HeaderCell>{col.title}</HeaderCell>
            <Cell dataKey={col.key} />
          </Column>
        );
      })}
    </Table>
  )
}
