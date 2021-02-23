import React from 'react'
import { Col, Container, Row } from 'rsuite';

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

export default function({ gear, slot }) {
  const item = gear && gear[slot];

  function renderGemSlot() {
    return <></>;
  }

  function renderItem() {
    const itemClass = `q${item.quality}`
    const itemGems = 'gems=' + item.sockets.map(sk => sk.gem && sk.gem.id).join(':')
    const itemEnchant = `ench=${item.enchant && item.enchant.id}`
    const itemSet = ''//TODO: 'pcs=' + gear.all().map(it => it.id).join(':')

    return (
      <Row xsPush={24}>
        <Col>
          <a
            href={`https://70.wowfan.net/en?item=${item.id}`}
            className={itemClass} rel={`${itemGems}&amp;${itemEnchant}&amp;${itemSet}`}
          >
            <img src={`${imgRoot}/${item.icon}`} />
          </a>
        </Col>
        <Col>
          <span>{titles[slot]}</span>
        </Col>
      </Row>
    );
  }

  function renderBlank() {
    return (
      <Row>
        <Col>
          <img
            style={{ border: '1px solid #AAA' }}
            src={`${bgImgRoot}/${bgImages[slot]}`}
          />
        </Col>
        <Col>
          <span>{titles[slot]}</span>
        </Col>
      </Row>
    );
  }

  return (
    <>{item ? renderItem() : renderBlank()}</>
  );
}
