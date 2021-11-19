import React, { useState } from 'react';
import _ from 'lodash';
import { Checkbox, Content, Container, Dropdown, Icon, Input, InputGroup, Grid, Row, Col, Panel, Table } from 'rsuite';
import { allEpCategories } from '../data/constants';
import ItemTooltip from './item_tooltip';
import { filterByItemName, filter1HOnly, itemsForSlot } from '../util/items';
import EPOptions from '../ep/ep_options';

const { Column, HeaderCell, Cell } = Table;

const classes = [{
  label: 'Hunter (Beast Mastery)',
  value: 'hunter_bm'
},{
  label: 'Hunter(Survival)',
  value: 'hunter_surv'
},{
  label: 'Mage (Arcane)',
  value: 'mage_arcane'
},{
  label: 'Mage (Fire)',
  value: 'mage_fire'
},{
  label: 'Mage (Frost)',
  value: 'mage_frost'
},{
  label: 'Priest (Shadow)',
  value: 'priest_shadow'
},{
  label: 'Rogue (Assassination)',
  value: 'rogue_assassination'
},{
  label: 'Rogue (Combat)',
  value: 'rogue_combat'
},{
  label: 'Shaman (Elemental)',
  value: 'shaman_ele'
},{
  label: 'Shaman (Enhancement)',
  value: 'shaman_enh'
},{
  label: 'Warlock (Afflicton + Ruin)',
  value: 'warlock_affliction_ruin'
},{
  label: 'Warlock (Destruction + Fire)',
  value: 'warlock_destruction_fire'
},{
  label: 'Warlock (Destruction + Shadow)',
  value: 'warlock_destruction_shadow'
},{
  label: 'Warrior (Arms)',
  value: 'warrior_arms'
},{
  label: 'Warrior (Fury)',
  value: 'warrior_fury'
},{
  label: 'Warrior (Kebab)',
  value: 'warrior_kebab'
}];

function IconCell({ rowData, dataKey, ...props }) {
  const cellValue = rowData[dataKey]
  if(cellValue) {
    return (
      <Cell {...props}>
        <ItemTooltip item={rowData} allowClick={true}>
          <img style={{ border: '1px solid #AAA', marginTop: '-10px', marginLeft: '-13px' }} src={`icons/${cellValue}`} />
        </ItemTooltip>
      </Cell>
    )
  }

  return null;
}

function NameCell({ rowData, dataKey, ...props }) {
  const cellValue = rowData.displayName || rowData.name
  return (
    <Cell {...props}>
      <ItemTooltip item={rowData} allowClick={true}>
        <p className={`q${rowData.quality}`} style={{ fontWeight: 800 }}>{cellValue}</p>
      </ItemTooltip>
    </Cell>
  )
}

function ItemLevelCell({ rowData, dataKey, ...props }) {
  const cellValue = rowData[dataKey]
  return (
    <Cell {...props}>
      <p>{cellValue}</p>
    </Cell>
  )
}

function EpCell({ rowData, dataKey, ...props }) {
  const cellValue = rowData[dataKey]
  return (
    <Cell {...props}>
      <p>{cellValue}</p>
    </Cell>
  )
}

export default function({ epOptions, dispatch }) {
  const [klass, setKlass] = useState('hunter_bm');
  const [itemPhase, setItemPhase] = useState(2);
  const [epCategory, setEpCategory] = useState('phase2');

  function getCharacter() {
    return {
      class: klass.substring(0, klass.indexOf('_')),
      epCategory,
      epSpec: klass,
      gear: {
        rangedTotemLibram: null
      }
    }
  }

  function EPPanel({ header, slotName, epOptions }) {
    const [filter, setFilter] = useState('');
    const [oneHandOnly, setOneHandOnly] = useState(slotName === 'mainHand');

    const filters = [filterByItemName(filter), ...(oneHandOnly ? [filter1HOnly()] : [])]
    const allRowData = itemsForSlot(slotName, getCharacter(), itemPhase, null, null, filters, epOptions)
    return (
      <Panel header={header} collapsible bordered defaultExpanded={true} style={{ marginBottom: 10 }}>
        <InputGroup inside style={{ margin: '0px 0 15px 0' }}>
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
          <Column flexGrow={1}>
            <HeaderCell>EP</HeaderCell>
            <EpCell dataKey="ep" />
          </Column>
          <Column flexGrow={1}>
            <HeaderCell>ilvl</HeaderCell>
            <ItemLevelCell dataKey="itemLevel" />
          </Column>
        </Table>
      </Panel>
    )
  }

  function KlassSelect() {
    function onSelect(kls) {
      setKlass(kls)
    }

    const klassEntry = classes.find(kls => kls.value === klass)
    const klassLabel = klassEntry ? klassEntry.label : 'None'
    return (
      <>
        <Dropdown title="Class/Spec">
          {classes.map(kls => {
            return <Dropdown.Item key={kls.value} eventKey={kls.value} onSelect={onSelect}>{kls.label}</Dropdown.Item>
          })}
        </Dropdown>
        <span>{klassLabel}</span>
      </>
    );
  }

  function PhaseSelect() {
    const allPhases = [1, 2, 3, 4, 5]

    function onSelect(phase) {
      setItemPhase(phase)
    }

    return (
      <>
        <Dropdown title="Item Filter">
          {allPhases.map(phase => {
            return <Dropdown.Item key={phase} eventKey={phase} onSelect={onSelect}>Phase {phase}</Dropdown.Item>
          })}
        </Dropdown>
        <span>Phase {itemPhase}</span>
      </>
    );
  }

  function EpSelect() {
    const epCategoryEntry = allEpCategories.find(epc => epc.key == epCategory)
    if(epCategoryEntry == null) return null;

    const epCategoryName = epCategoryEntry.name;

    function onSelect(epCategory) {
      setEpCategory(epCategory)
    }

    return (
      <>
        <Dropdown title="EP Category">
          {allEpCategories.map(epCategory => {
            return <Dropdown.Item key={epCategory.key} eventKey={epCategory.key} onSelect={onSelect}>{epCategory.name}</Dropdown.Item>
          })}
        </Dropdown>
        <span>{epCategoryName}</span>
      </>
    );
  }

  return (
    <Content>
      <Container style={{ marginTop: 20, marginBottom: 20, marginLeft: 15 }}>
        <Row>
          <h5>Gear Equivalence Points</h5>
          <p>This page is meant to be used as a quick comparison between candidates for an item slot, using TBCSim EP.</p>
          <br/>
          <h5>Notes and Caveats</h5>
          <ul>
            <li>Item procs and set bonuses are not currently included in total item EP when selecting items in the UI (Procs and sets are fully modeled in the sim)</li>
          </ul>
        </Row>
      </Container>
      <Grid fluid={true}>
        <Row>
          <KlassSelect />
          <PhaseSelect />
          <EpSelect />
          <EPOptions epOptions={epOptions} dispatch={dispatch} />
        </Row>
        <Container style={{ margin: 15 }}>
        {(!klass || !epCategory || !itemPhase) ?
          <Row>Please select a class and EP category</Row>
          :
          <Row>
            <Col xs={8}>
              <EPPanel header='Head' slotName='head' epOptions={epOptions} bordered />
              <EPPanel header='Neck' slotName='neck' epOptions={epOptions} bordered />
              <EPPanel header='Shoulders' slotName='shoulders' epOptions={epOptions} bordered />
              <EPPanel header='Back' slotName='back' epOptions={epOptions} bordered />
              <EPPanel header='Chest' slotName='chest' epOptions={epOptions} bordered />
              <EPPanel header='Wrists' slotName='wrists' epOptions={epOptions} bordered />
            </Col>
            <Col xs={8}>
              <EPPanel header='Main Hand' slotName='mainHand' epOptions={epOptions} bordered />
              <EPPanel header='Off Hand' slotName='offHand' epOptions={epOptions} bordered />
              <EPPanel header='Ranged' slotName='rangedTotemLibram' epOptions={epOptions} bordered />
            </Col>
            <Col xs={8}>
              <EPPanel header='Hands' slotName='hands' epOptions={epOptions} bordered />
              <EPPanel header='Waist' slotName='waist' epOptions={epOptions} bordered />
              <EPPanel header='Legs' slotName='legs' epOptions={epOptions} bordered />
              <EPPanel header='Feet' slotName='feet' epOptions={epOptions} bordered />
              <EPPanel header='Ring' slotName='ring1' epOptions={epOptions} bordered />
              <EPPanel header='Trinket' slotName='trinket1' epOptions={epOptions} bordered />
            </Col>
          </Row>
        }
        </Container>
      </Grid>
    </Content>
  )
}
