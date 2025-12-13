import React, { useState } from 'react';

import SpellTooltip from './spell_tooltip';
import GearSelector from './gear_selector';

export default function({ character, item, phase, enchant, enchantType, slotName, onSelect }) {
  const [selectorVisible, setSelectorVisible] = useState(false);

  function onEnchantClick(e) {
    e.preventDefault();
    e.stopPropagation();
    setSelectorVisible(true)
  }

  function onEnchantSelect(enchant) {
    setSelectorVisible(false)
    onSelect(enchant)
  }

  if(!item) {
    return null;
  }

  if(!enchant) {
    const missingMsg = enchantType === 'tempEnchants' ? 'No temp enchant' : 'No enchant'
    return (
      <div onClick={onEnchantClick}>
          <span style={{ cursor: 'pointer' }}>{missingMsg}</span>
          <GearSelector
            character={character}
            type={enchantType}
            phase={phase}
            item={item}
            TooltipComponent={SpellTooltip}
            slotName={slotName}
            visible={selectorVisible}
            setVisible={setSelectorVisible}
            onSelect={onEnchantSelect}
          />
      </div>
    )
  }

  return (
    <div onClick={onEnchantClick}>
      <SpellTooltip spell={enchant}>
        <span className="q2">{enchant.displayName}</span>
        <GearSelector
          character={character}
          type={enchantType}
          phase={phase}
          item={item}
          TooltipComponent={SpellTooltip}
          slotName={slotName}
          visible={selectorVisible}
          setVisible={setSelectorVisible}
          onSelect={onEnchantSelect}
        />
      </SpellTooltip>
    </div>
  )
}
