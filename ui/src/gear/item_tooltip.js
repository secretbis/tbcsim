import React from 'react';
import _ from 'lodash';

export default function({ gear, item, isMetaGem=false, children }) {
  if(item) {
    // TODO: Meta gem tooltips don't seem to work, regardless of how I set IDs
    // const allGemIds = []
    // if(isMetaGem) {
    //   _.values(gear).forEach(item => {
    //     item && item.sockets && item.sockets.forEach(sk => {
    //       if(sk.gem && sk.gem.id) {
    //         allGemIds.push(sk.gem.id)
    //       }
    //     })
    //   })
    // }

    gear = gear || {};
    const itemClass = `q${item.quality}`
    const itemGems = 'gems=' + item.sockets.map(sk => sk.gem && sk.gem.id).join(':')
    const itemEnchant = item.enchant ? `ench=${item.enchant.id}` : ''
    const itemSet = 'pcs=' + _.values(gear).filter(it => !!it).map(it => it.id).join(':')
    const suffixStr = `${itemGems}&${itemEnchant}&${itemSet}`

    return (
      <a
        href={`https://tbc.wowhead.com/?item=${item.id}&${suffixStr}`}
        className={itemClass}
        rel={suffixStr}
        onClick={e => e.preventDefault()}
        style={{ textDecoration: 'none' }}
      >
        {children}
      </a>
    )
  }

  return <>{children}</>
}
