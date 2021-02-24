import React, { useState } from 'react';
import _ from 'lodash';
import { Input, InputGroup, Icon, Modal, Table } from 'rsuite';

import * as tbcsim from 'tbcsim';

const { Column, HeaderCell, Cell } = Table;

export default function({ inventorySlots, itemClasses, visible, setVisible, dispatch }) {
  const [filter, setFilter] = useState(null);

  function getItemsForSlot() {
    let items = [];
    for(const inventorySlot of inventorySlots) {
      items = [...items, ...(tbcsim.data.Items.bySlot.get_35(inventorySlot) || [])];
    }

    // Filter again by equippable item subclasses
    const filtered = _.filter(items, item => {
      const itemClass = item.itemClass._ordinal;

      if(itemClasses.itemClasses.includes(itemClass)) {
        const itemSubclass = item.itemSubclass._itemClassOrdinal;
        const subclasses = itemClasses.itemSubclasses[itemClass]

        if(subclasses.includes(itemSubclass)) {
          const matchesName = filter ? item.name.toLowerCase().includes(filter.toLowerCase()) : true
          return matchesName
        }
      }

      return false;
    });

    return _.sortBy(filtered, 'itemLevel').reverse()
  }

  function getGems() {

  }

  function onRowClick(item) {
    debugger
  }

  function onHide() {
    setVisible(false);
    setFilter(null);
  }

  if(!inventorySlots || !visible) {
    return null;
  }

  function IconCell({ rowData, dataKey, ...props }) {
    const cellValue = rowData[dataKey]
    return (
      <Cell { ...props} style={{ padding: 'none' }}>
        <img style={{  marginTop: '-10px', marginLeft: '-13px' }} src={`icons/${cellValue}`} />
      </Cell>
    )
  }

  function NameCell({ rowData, dataKey, ...props }) {
    const cellValue = rowData[dataKey]
    return (
      <Cell { ...props}>
        <p className={`q${rowData.quality}`} style={{ fontWeight: 800 }}>{cellValue}</p>
      </Cell>
    )
  }

  return (
    <Modal show={visible} onHide={onHide}>
      <Modal.Header>
        <Modal.Title>Select an Item</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <InputGroup inside style={{ margin: '15px 0 15px 0' }}>
          <Input onChange={value => setFilter(value)} />
          <InputGroup.Button>
            <Icon icon="search" />
          </InputGroup.Button>
        </InputGroup>

        <Table height={400} rowHeight={60} data={getItemsForSlot()}>
          <Column width={55}>
            <HeaderCell></HeaderCell>
            <IconCell dataKey="icon" />
          </Column>
          <Column flexGrow={4}>
            <HeaderCell>Name</HeaderCell>
            <NameCell dataKey="name" />
          </Column>
          <Column flexGrow={1}>
            <HeaderCell>ilvl</HeaderCell>
            <Cell dataKey="itemLevel" />
          </Column>
        </Table>
      </Modal.Body>
    </Modal>
  );
}
