import React from 'react';
import _ from 'lodash';

export default function({ spell, children }) {
  if(spell) {
    const spellClass = 'q1';

    return (
      <a
        href={`https://70.wowfan.net/en?spell=${spell.id}`}
        className={spellClass}
        onClick={e => e.preventDefault()}
        style={{ textDecoration: 'none' }}
      >
        {children}
      </a>
    )
  }

  return <>{children}</>
}
