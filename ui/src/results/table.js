import React from 'react';
import { Container, Table } from 'rsuite';

import { noop } from './formatters'

const { Column, HeaderCell, Cell } = Table;

export default ({ title, data, columnInfo }) => {

  function renderCell(col, row) {
    const formatter = col.formatter || noop
    const result = formatter(row[col.key])
    return result === "NaN" ? '-' : result;
  }

  return (
    <Container>
      <h5 style={{ marginBottom: '5px' }}>{title}</h5>
      <Table
        autoHeight={true}
        data={data}
      >
        {columnInfo.map((col, idx) => {
          return (
            <Column key={idx} flexGrow={col.flex || 1} minWidth={col.minWidth}>
              <HeaderCell title={col.title}>{col.title}</HeaderCell>
              <Cell>
                {row => renderCell(col, row)}
              </Cell>
            </Column>
          );
        })}
      </Table>
    </Container>
  )
}
