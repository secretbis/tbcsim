import React from 'react';

import { Modal, Table } from 'rsuite';

export default function({ visible, setVisible, onSelect }) {
  function getItemsForSlot() {

  }

  function onHide() {
    setVisible(false)
  }

  return (
    <Modal show={visible} onHide={onHide}>
      <Modal.Header>
        <Modal.Title>Select an Item</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Table />
      </Modal.Body>
    </Modal>
  );
}
