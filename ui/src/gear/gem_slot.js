import React, { useState } from 'react';

import { itemClasses, gemSubclasses as gsc } from '../data/constants';
import ItemTooltip from './item_tooltip';
import GearSelector from './gear_selector';

const socketImages = {
  blue: 'sockets/blue.png',
  meta: 'sockets/meta.png',
  red: 'sockets/red.png',
  yellow: 'sockets/yellow.png',
};

const nonMetaIC = {
  itemClasses: [itemClasses.gem],
  itemSubclasses: {
    [itemClasses.gem]: [gsc.red, gsc.blue, gsc.yellow, gsc.purple, gsc.green, gsc.orange]
  }
};

const metaIC = {
  itemClasses: [itemClasses.gem],
  itemSubclasses: {
    [itemClasses.gem]: [gsc.meta]
  }
}

export default function({ socket, character, onSelect }) {
  const [selectorVisible, setSelectorVisible] = useState(false);

  const color = socket._color_0._name_2.toLowerCase();
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
          inventorySlots={[0]}
          itemClasses={isMetaGem ? metaIC : nonMetaIC}
          visible={selectorVisible}
          setVisible={setSelectorVisible}
          onSelect={onGemSelect}
        />
      </ItemTooltip>
    </span>
  )
}
