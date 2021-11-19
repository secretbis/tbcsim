import React, { useState } from 'react';
import _ from 'lodash';
import { Checkbox, Button, Modal } from 'rsuite';

export default function({ epOptions, dispatch }) {
  const [modalOpen, setModalOpen] = useState(false);

  function onOpen() {
    setModalOpen(true)
  }

  function onHide() {
    setModalOpen(false);
  }

  function onChange(key, checked) {
    dispatch({ type: `epOptions.${key}`, value: checked });
  }

  const hitZero = _.get(epOptions, 'hitZero');

  return (
    <span style={{ marginLeft: 10 }}>
      <Button onClick={onOpen}>EP Options</Button>
      <Modal show={modalOpen} size='sm' onHide={onHide} style={{ maxHeight: '80vh' }}>
        <Modal.Header style={{ marginBottom: 10 }}>
          <Modal.Title style={{ marginBottom: 10 }}>EP Options</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div>
            <Checkbox key='hitZero' value='hitZero' checked={hitZero} onChange={onChange}>Value hit as zero</Checkbox>
          </div>
          <div style={{ marginLeft: 36, color: '#AAA' }}>
            <em>Check this option if you are hitcapped, and looking for pieces without hit</em>
          </div>
        </Modal.Body>
      </Modal>
    </span>
  )
}
