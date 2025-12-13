import React, { useState } from 'react';
import _ from 'lodash';
import { Checkbox, Input, InputGroup, Modal, Table } from 'rsuite';
import { Icon } from '@rsuite/icons';
import { FaSearch } from "react-icons/fa";

import { filterByItemName, filter1HOnly, itemsForSlot } from '../util/items';

import ItemTooltip from './item_tooltip';

const { Column, HeaderCell, Cell } = Table;

export default function({ character, phase, type, item, slotName, TooltipComponent, visible, setVisible, onSelect, epOptions }) {
  const [filter, setFilter] = useState('');
  const [oneHandOnly, setOneHandOnly] = useState(true);
  const [modalFullyShown, setModalFullyShown] = useState(false);

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
            <img style={{ border: '1px solid #AAA', borderRadius: 5, marginTop: '-10px', marginLeft: '-13px' }} src={`icons/${cellValue}`} />
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
          <span className={`q${rowData.quality}`} style={{ fontWeight: 800 }}>{cellValue}</span>
        </TooltipComponent>
      </Cell>
    )
  }

  function ItemLevelCell({ rowData, dataKey, ...props }) {
    const cellValue = rowData[dataKey]
    return (
      <Cell {...props} onClick={(e) => onRowClick(rowData, e)}>
        <span>{cellValue}</span>
      </Cell>
    )
  }

  function EpCell({ rowData, dataKey, ...props }) {
    const cellValue = rowData[dataKey]
    return (
      <Cell {...props} onClick={(e) => onRowClick(rowData, e)}>
        <span>{cellValue}</span>
      </Cell>
    )
  }

  function renderModalBody() {
    const filters = [filterByItemName(filter), ...(oneHandOnly && slotName === 'mainHand' ? [filter1HOnly()] : [])]
    const allRowData = itemsForSlot(slotName, character, phase, type, item, filters, epOptions)
    return (
      <>
        <InputGroup inside style={{ margin: '15px 0 15px 0' }}>
          <Input placeholder={'Type to filter'} onChange={value => setFilter(value)} />
          <InputGroup.Button>
            <Icon as={FaSearch} />
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
    <Modal open={true} size={'sm'} onEntered={onEntered} onHide={onHide} onClose={onHide}>
      <Modal.Header>
        <Modal.Title>Select an Item (Phase {phase})</Modal.Title>
      </Modal.Header>
      <Modal.Body style={{ maxHeight: 500 }}>
        {modalFullyShown ? renderModalBody() : null}
      </Modal.Body>
    </Modal>
  );
}
