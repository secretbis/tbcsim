import React, { useState } from 'react';

import ItemTooltip from './item_tooltip';
import GearSelector from './gear_selector';
import { kprop } from '../util/util';

const socketImages = {
  blue: 'sockets/blue.png',
  meta: 'sockets/meta.png',
  red: 'sockets/red.png',
  yellow: 'sockets/yellow.png',
};

export default function({ phase, socket, character, onSelect, epOptions }) {
  const [selectorVisible, setSelectorVisible] = useState(false);

  const color = kprop(kprop(socket, 'color'), 'name', '').toLowerCase();
  const gem = socket.gem;
  const icon = gem ? `icons/${gem.icon}` : socketImages[color];

  function onGemClick(e) {
    e.preventDefault();
    e.stopPropagation();
    setSelectorVisible(true)
  }

  function onGemSelect(gem) {
    setSelectorVisible(false)
    onSelect(gem)
  }

  const isMetaGem = color == 'meta'
  return (
    <span onClick={onGemClick}>
      <ItemTooltip item={gem} isMetaGem={isMetaGem} gear={character.gear}>
        <img src={icon} style={{ width: 20, height: 20, marginRight: 5, cursor: 'pointer' }} />
        <GearSelector
          character={character}
          phase={phase}
          slotName={isMetaGem ? 'metaGem' : 'gem'}
          visible={selectorVisible}
          setVisible={setSelectorVisible}
          onSelect={onGemSelect}
          epOptions={epOptions}
        />
      </ItemTooltip>
    </span>
  )
}
