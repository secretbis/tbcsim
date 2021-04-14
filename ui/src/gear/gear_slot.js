import React, { useState }  from 'react'
import { Col, Row } from 'rsuite';

import ItemTooltip from './item_tooltip';
import GearSelector from './gear_selector';
import EnchantSlot from './enchant_slot';
import GemSlot from './gem_slot';

import { inventorySlots as inv, itemClasses as ic } from '../data/constants';

const defaultWidth = '55px';
const bgImages = {
  head: 'head.jpg',
  neck: 'neck.jpg',
  shoulders: 'shoulders.jpg',
  back: 'back.jpg',
  chest: 'chest.jpg',
  wrists: 'wrists.jpg',
  mainHand: 'mainhand.jpg',
  offHand: 'offhand.jpg',
  hands: 'hands.jpg',
  waist: 'waist.jpg',
  legs: 'legs.jpg',
  feet: 'feet.jpg',
  ring1: 'ring.jpg',
  ring2: 'ring.jpg',
  trinket1: 'trinket.jpg',
  trinket2: 'trinket.jpg',
  rangedTotemLibram: 'ranged.jpg',
  ammo: 'ranged.jpg',
};

const titles = {
  head: 'HEAD',
  neck: 'NECK',
  shoulders: 'SHOULDERS',
  back: 'BACK',
  chest: 'CHEST',
  wrists: 'WRISTS',
  mainHand: 'MAIN HAND',
  offHand: 'OFF HAND',
  hands: 'HANDS',
  waist: 'WAIST',
  legs: 'LEGS',
  feet: 'FEET',
  ring1: 'RING 1',
  ring2: 'RING 2',
  trinket1: 'TRINKET 1',
  trinket2: 'TRINKET 2',
  rangedTotemLibram: 'RANGED/RELIC',
  ammo: 'AMMO',
};

export default function({ character, slotName, inventorySlots, itemClasses, width=defaultWidth, dispatch }) {
  const item = character && character.gear && character.gear[slotName];
  const itemImgStyles = { border: '1px solid #AAA', height: width, width }

  const [selectorVisible, setSelectorVisible] = useState(false);

  function onClick(e) {
    e.stopPropagation();
    e.preventDefault();
    setSelectorVisible(true);
  }

  function onItemSelect(item) {
    // Clean sockets and enchants
    item.sockets.forEach(sk => sk.gem = null);
    item.enchant = null;
    item.tempEnchant = null;
    dispatch({ type: 'updateGearSlot', value: { [slotName]: item }, slotName, item })
  }

  function onGemSelect(gem, idx) {
    item.sockets[idx].gem = gem
    dispatch({ type: 'updateGearSlot', value: { [slotName]: item } })
  }

  function onEnchantSelect(enchant) {
    item.enchant = enchant
    dispatch({ type: 'updateGearSlot', value: { [slotName]: item } })
  }

  function onTempEnchantSelect(enchant) {
    item.tempEnchant = enchant
    dispatch({ type: 'updateGearSlot', value: { [slotName]: item } })
  }

  function renderItemLabel() {
    return (
      <Row>
        <Col xs={7}>
          <span>{titles[slotName]}</span>
        </Col>
      </Row>
    );
  }

  function renderItem() {
    const slotCanEnchant = !['trinket1', 'trinket2', 'neck', 'waist', 'ammo'].includes(slotName);
    const itemCanEnchant = slotName == 'rangedTotemLibram' ? ![inv.thrown, inv.ranged_right, inv.relic].includes(item.inventorySlot) : item.inventorySlot !== inv.holdable_tome;
    const itemCanTempEnchant = (slotName == 'mainHand' || slotName == 'offHand') && item.itemClass && item.itemClass._ordinal === ic.weapon;

    return (
      <Row style={{ padding: '5px' }} onClick={onClick}>
        <Col xs={5}>
          <ItemTooltip item={item} gear={character.gear}>
            <img style={itemImgStyles} src={`icons/${item.icon}`} />
          </ItemTooltip>
        </Col>
        <Col>
          <Row>
            <ItemTooltip item={item} gear={character.gear}>
              <p style={{ fontSize: '16px', fontWeight: 800 }}>{item.name}</p>
            </ItemTooltip>
            {item.sockets.map((sk, idx) => {
              return <GemSlot key={idx} socket={sk} character={character} onSelect={(gem) => onGemSelect(gem, idx)} />
            })}
            {slotCanEnchant && itemCanEnchant ?
              <EnchantSlot enchantType={'enchants'} item={item} enchant={item && item.enchant} inventorySlots={inventorySlots} onSelect={onEnchantSelect} />
            : null}
            {itemCanTempEnchant ?
              <EnchantSlot enchantType={'tempEnchants'} item={item} enchant={item && item.tempEnchant} inventorySlots={inventorySlots} onSelect={onTempEnchantSelect} />
            : null}
          </Row>
        </Col>
      </Row>
    );
  }

  function renderBlank() {
    return (
      <Row style={{ padding: '5px' }}>
        <Col xs={5} onClick={onClick}>
          <img
            style={itemImgStyles}
            src={`slotbg/${bgImages[slotName]}`}
          />
        </Col>
        {renderItemLabel()}
      </Row>
    );
  }

  return (
    <>
      {item ? renderItem() : renderBlank()}
      <GearSelector
        character={character}
        allowableClasses={[character.class]}
        inventorySlots={inventorySlots}
        itemClasses={itemClasses}
        visible={selectorVisible}
        setVisible={setSelectorVisible}
        onSelect={onItemSelect}
      />
    </>
  );
}
