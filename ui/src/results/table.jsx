import React from 'react';
import { Container, Table, Row, Col } from 'rsuite';

import { noop } from './formatters'
import TableExport from '../data/export';

const { Column, HeaderCell, Cell } = Table;

export default ({ title, data, columnInfo, icons=true }) => {

  function renderCell(col, row) {
    const formatter = col.formatter || noop
    const result = formatter(row[col.key])
    return result === "NaN" ? '-' : result;
  }

  function iconColumn() {
    if(!icons) return null;

    return (
      <Column width={38}>
        <HeaderCell title=''></HeaderCell>
        <Cell>
          {row => row ? <img style={{ marginTop: -6, marginLeft: -5, border: '1px solid #AAA', borderRadius: 3 }} height={32} width={32} src={`icons/${row.icon}`} /> : null }
        </Cell>
      </Column>
    );
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
        {iconColumn()}
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
