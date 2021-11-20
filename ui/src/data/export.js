import React, { useState } from 'react';
import _ from 'lodash';
import { Container, Button, Modal, Nav } from 'rsuite';

function delimitTable(columnInfo, data, delimiter) {
  let headerStr = columnInfo.map(c => c.title).join(delimiter) + '\n';
  let dataStr = (data && data.map && data.map(d => {
    return columnInfo.map(c => {
      const result = d[c.key];
      return Number.isNaN(result) ? '0' : result.toLocaleString(undefined, {
        maximumFractionDigits: 2,
        minimumFractionDigits: 2,
        useGrouping: false
      });;
    }).join(delimiter);
  }).join('\n')) || '';

  return headerStr + dataStr;
}

const exporters = {
  'CSV': function(columnInfo, data) {
    return delimitTable(columnInfo, data, ',');
  },
  'TSV': function(columnInfo, data) {
    return delimitTable(columnInfo, data, '	');
  }
};

export default function({ columnInfo, data }) {
  const [modalOpen, setModalOpen] = useState(false);
  const [activeTab, setActiveTab] = useState('CSV')

  function onOpen() {
    setModalOpen(true)
  }

  function onHide() {
    setModalOpen(false);
  }

  function onSelect(evt) {
    setActiveTab(evt)
  }

  function renderTab() {
    const exportData = exporters[activeTab](columnInfo, data)

    return (
      <>
        <Button onClick={() => { navigator.clipboard.writeText(exportData); }}>Copy to Clipboard</Button>
        <textarea
          style={{ color: '#000', resize: 'none' }}
          value={exportData}
          rows={5}
          wrap={'soft'}
        />
      </>
    );
  }

  return (
    <span style={{ marginLeft: 10 }}>
      <Button appearance='ghost' onClick={onOpen}>Export</Button>
      <Modal show={modalOpen} full size='lg' onHide={onHide} style={{ display: 'inline-block', maxHeight: '80vh' }}>
        <Modal.Header style={{ marginBottom: 10 }}>
          <Modal.Title style={{ marginBottom: 10 }}>Export</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Container>
            <Nav appearance='subtle' activeKey={activeTab} onSelect={onSelect}>
              <Nav.Item eventKey={'CSV'}>
                <span style={{ fontSize: 20 }}>CSV</span>
              </Nav.Item>
              <Nav.Item eventKey={'TSV'}>
                <span style={{ fontSize: 20 }}>TSV</span>
              </Nav.Item>
            </Nav>
            {renderTab()}
          </Container>
        </Modal.Body>
      </Modal>
    </span>
  )
}
