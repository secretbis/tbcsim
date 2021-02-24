import React, { useState }  from 'react'
import { Col, Row } from 'rsuite';

import GearSelector from './gear_selector';

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
  ranged: 'ranged.jpg',
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
  ranged: 'RANGED',
  ammo: 'AMMO',
}

const bgImgRoot = 'slotbg'
const imgRoot = 'icons'

const socketImages = {
  blue: 'sockets/blue.png',
  meta: 'sockets/meta.png',
  red: 'sockets/red.png',
  yellow: 'sockets/yellow.png',
}

export default function({ character, slotName, inventorySlots, itemClasses, width=defaultWidth, dispatch }) {
  const item = character && character.gear && character.gear[slotName];
  const itemImgStyles = { border: '1px solid #AAA', height: width, width }

  const [selectorVisible, setSelectorVisible] = useState(false);

  function onClick() {
    setSelectorVisible(true);
  }

  function renderGemSlot(socket, idx) {
    const color = socket._color_0._name_2.toLowerCase();
    const gem = socket.gem;
    const icon = gem ? `${imgRoot}/${gem.icon}` : socketImages[color];

    return <img src={icon} style={{ width: 20, height: 20, marginRight: 5 }} />;
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
    const itemClass = `q${item.quality}`
    const itemGems = 'gems=' + item.sockets.map(sk => sk.gem && sk.gem.id).join(':')
    const itemEnchant = item.enchant ? `ench=${item.enchant.id}` : ''
    const itemSet = ''//TODO: 'pcs=' + gear.all().map(it => it.id).join(':')

    return (
      <Row style={{ padding: '5px' }} onClick={onClick}>
        <Col xs={5}>
          <a
            href={`https://70.wowfan.net/en?item=${item.id}`}
            className={itemClass} rel={`${itemGems}&amp;${itemEnchant}&amp;${itemSet}`}
            onClick={e => e.preventDefault() && onClick()}
          >
            <img style={itemImgStyles} src={`${imgRoot}/${item.icon}`} />
          </a>
        </Col>
        <Col>
          <Row>
            <a
              href={`https://70.wowfan.net/en?item=${item.id}`}
              target='_blank'
              className={itemClass} rel={`${itemGems}&amp;${itemEnchant}&amp;${itemSet}`}
            >
              <p style={{ fontSize: '16px', fontWeight: 800 }}>{item.name}</p>
            </a>
            {item.sockets.map((sk, idx) => {
              return renderGemSlot(sk, idx);
            })}
            {!['trinket1', 'trinket2', 'neck'].includes(slotName) ?
              item.enchant ? <p>{item.enchant.name}</p> : <p>No enchant</p>
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
            src={`${bgImgRoot}/${bgImages[slotName]}`}
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
          inventorySlots={inventorySlots}
          itemClasses={itemClasses}
          visible={selectorVisible}
          setVisible={setSelectorVisible}
          dispatch={dispatch}
        />
    </>
  );
}
