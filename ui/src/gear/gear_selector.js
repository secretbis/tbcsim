import React, { useState } from 'react';
import _, { trimEnd } from 'lodash';
import { Input, InputGroup, Icon, Modal, Table } from 'rsuite';

import ItemTooltip from './item_tooltip';

import * as tbcsim from 'tbcsim';

const { Column, HeaderCell, Cell } = Table;

export default function({ type, item, TooltipComponent, inventorySlots, itemClasses, allowableClasses, visible, setVisible, onSelect }) {
  const [filter, setFilter] = useState(null);

  TooltipComponent = TooltipComponent || ItemTooltip

  let baseData = tbcsim.data.Items
  if(type === "enchants") {
    baseData = tbcsim.data.Enchants
  }

  function getItemsForSlot() {
    let items = [];
    for(const inventorySlot of inventorySlots) {
      items = [...items, ...(baseData.bySlot.get_35(inventorySlot) || [])];
    }

    items = items.map(i => i(item))

    // Filter again by equippable item subclasses, if provided
    const filtered = _.filter(
      _.filter(items, item => {
        if(itemClasses) {
          const itemClass = item.itemClass._ordinal;

          if(itemClasses.itemClasses.includes(itemClass)) {
            const itemSubclass = item.itemSubclass._itemClassOrdinal;
            const subclasses = itemClasses.itemSubclasses[itemClass]

            if(subclasses.includes(itemSubclass)) {
              const matchesName = filter ? (item.displayName || item.name).toLowerCase().includes(filter.toLowerCase()) : true
              return matchesName
            }
          }

          return false;
        }

        return true;
      }), item => {
        // Filter by allowable classes
        if(allowableClasses) {
          return item.allowableClasses == null || item.allowableClasses.some(it => {
            return allowableClasses.map(it => it.toUpperCase()).includes(it._name_2.toUpperCase());
          })
        }
        return true;
      });

    return _.sortBy(filtered, 'itemLevel').reverse()
  }

  function onRowClick(item, e) {
    e.preventDefault()
    e.stopPropagation()

    setVisible(false)
    onSelect(item)
  }

  function onHide(e) {
    e.preventDefault()
    e.stopPropagation()

    setVisible(false);
    setFilter(null);
  }

  if(!inventorySlots || !visible) {
    return null;
  }

  function IconCell({ rowData, dataKey, ...props }) {
    const cellValue = rowData[dataKey]
    if(cellValue) {
      return (
        <Cell {...props} onClick={(e) => onRowClick(rowData, e)}>
          <TooltipComponent item={rowData} enchant={rowData}>
            <img style={{ border: '1px solid #AAA', marginTop: '-10px', marginLeft: '-13px' }} src={`icons/${cellValue}`} />
          </TooltipComponent>
        </Cell>
      )
    }

    return null;
  }

  function NameCell({ rowData, dataKey, ...props }) {
    const cellValue = rowData.displayName || rowData.name
    return (
      <Cell {...props} onClick={(e) => onRowClick(rowData, e)}>
        <TooltipComponent item={rowData} enchant={rowData}>
          <p className={`q${rowData.quality}`} style={{ fontWeight: 800 }}>{cellValue}</p>
        </TooltipComponent>
      </Cell>
    )
  }

  function ItemLevelCell({ rowData, dataKey, ...props }) {
    const cellValue = rowData[dataKey]
    return (
      <Cell {...props} onClick={(e) => onRowClick(rowData, e)}>
        <p>{cellValue}</p>
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
            <ItemLevelCell dataKey="itemLevel" />
          </Column>
        </Table>
      </Modal.Body>
    </Modal>
  );
}
