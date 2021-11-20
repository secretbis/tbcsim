import React from 'react';
import { Container, Table, Row, Col } from 'rsuite';

import { noop } from './formatters'
import TableExport from '../data/export';

const { Column, HeaderCell, Cell } = Table;

export default ({ title, data, columnInfo }) => {

  function renderCell(col, row) {
    const formatter = col.formatter || noop
    const result = formatter(row[col.key])
    return result === "NaN" ? '-' : result;
  }

  return (
    <Container>
      <Row>
        <Col xs={12}><h5 style={{ marginBottom: '5px' }}>{title}</h5></Col>
        <Col xs={12} style={{ 'textAlign': 'right', marginTop: -12 }}><TableExport columnInfo={columnInfo} data={data} /></Col>
      </Row>
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
