import React, { useState } from 'react';
import _ from 'lodash';
import { Checkbox, Input, InputGroup, Icon, Modal, Table } from 'rsuite';

import { filterByItemName, filter1HOnly, itemsForSlot } from '../util/items';

import ItemTooltip from './item_tooltip';

const { Column, HeaderCell, Cell } = Table;

export default function({ character, phase, type, item, slotName, TooltipComponent, visible, setVisible, onSelect }) {
  const [filter, setFilter] = useState('');
  const [oneHandOnly, setOneHandOnly] = useState(true);
  const [modalFullyShown, setModalFullyShown] = useState('');

  TooltipComponent = TooltipComponent || ItemTooltip

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

  if(!visible) {
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
    const filters = [filterByItemName(filter), ...(oneHandOnly && slotName === 'mainHand' ? [filter1HOnly()] : [])]
    const allRowData = itemsForSlot(slotName, character, phase, type, item, filters)
    return (
      <>
        <InputGroup inside style={{ margin: '15px 0 15px 0' }}>
          <Input onChange={value => setFilter(value)} />
          <InputGroup.Button>
            <Icon icon="search" />
          </InputGroup.Button>
        </InputGroup>
        {slotName == 'mainHand' ?
        <InputGroup inside>
          <Checkbox checked={oneHandOnly} onChange={() => setOneHandOnly(!oneHandOnly)}>One-Hand Only</Checkbox>
        </InputGroup>
        : null}

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
        <Modal.Title>Select an Item (Phase {phase})</Modal.Title>
      </Modal.Header>
      <Modal.Body style={{ maxHeight: 500 }}>
        {modalFullyShown ? renderModalBody() : null}
      </Modal.Body>
    </Modal>
  );
}
