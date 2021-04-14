import React, { useState } from 'react';
import _ from 'lodash';
import { Input, InputGroup, Icon, Modal, Table } from 'rsuite';

import * as Constants from '../data/constants';
import { itemEp } from '../ep/ep_stats';
import ItemTooltip from './item_tooltip';

import * as tbcsim from 'tbcsim';

const { Column, HeaderCell, Cell } = Table;

export default function({ character, type, item, TooltipComponent, inventorySlots, itemClasses, allowableClasses, visible, setVisible, onSelect }) {
  const [filter, setFilter] = useState('');
  const [modalFullyShown, setModalFullyShown] = useState('');

  TooltipComponent = TooltipComponent || ItemTooltip

  let baseData = tbcsim.data.Items
  if(type === "enchants") {
    baseData = tbcsim.data.Enchants
  }
  if(type === "tempEnchants") {
    baseData = tbcsim.data.TempEnchants
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

            // Never filter the cloak slot on itemSubclass
            if(item.inventorySlot == Constants.inventorySlots.back) {
              return true
            }

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

    // Add item EP if we have a reference point
    if(character) {
      items = items.forEach(i => {
        const ep = itemEp(i, character.epCategory, character.epSpec)
        i.ep = ep
      })
    }

    const sortKey = character ? 'ep' : 'itemLevel';
    return _.sortBy(filtered, sortKey).reverse()
  }

  function onRowClick(item, e) {
    e.preventDefault();
    e.stopPropagation();

    setModalFullyShown(false);
    setVisible(false);
    setFilter('');

    onSelect(item);
  }

  function onHide(e) {
    e.preventDefault();
    e.stopPropagation();

    setModalFullyShown(false);
    setVisible(false);
    setFilter('');
  }

  function onEntered() {
    // For some reason, the table doesn't render right if the modal isn't fully rendered
    setModalFullyShown(true);
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

  function EpCell({ rowData, dataKey, ...props }) {
    const cellValue = rowData[dataKey]
    return (
      <Cell {...props} onClick={(e) => onRowClick(rowData, e)}>
        <p>{cellValue}</p>
      </Cell>
    )
  }

  function renderModalBody() {
    const allRowData = getItemsForSlot()
    return (
      <>
        <InputGroup inside style={{ margin: '15px 0 15px 0' }}>
          <Input onChange={value => setFilter(value)} />
          <InputGroup.Button>
            <Icon icon="search" />
          </InputGroup.Button>
        </InputGroup>

        <Table height={400} rowHeight={60} data={allRowData} affixHorizontalScrollbar={-1000}>
          <Column width={55}>
            <HeaderCell></HeaderCell>
            <IconCell dataKey="icon" />
          </Column>
          <Column flexGrow={4}>
            <HeaderCell>Name</HeaderCell>
            <NameCell dataKey="name" />
          </Column>
          {character ?
            <Column flexGrow={1}>
              <HeaderCell>EP</HeaderCell>
              <EpCell dataKey="ep" />
            </Column>
          : null}
          <Column flexGrow={1}>
            <HeaderCell>ilvl</HeaderCell>
            <ItemLevelCell dataKey="itemLevel" />
          </Column>
        </Table>
      </>
    )
  }

  if(!visible) return null

  return (
    <Modal show={true} height={600} onEntered={onEntered} onHide={onHide}>
      <Modal.Header>
        <Modal.Title>Select an Item</Modal.Title>
      </Modal.Header>
      <Modal.Body style={{ maxHeight: 500 }}>
        {modalFullyShown ? renderModalBody() : null}
      </Modal.Body>
    </Modal>
  );
}
