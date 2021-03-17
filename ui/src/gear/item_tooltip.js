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
    const itemSet = 'pcs=' + _.values(gear).map(it => it.id).join(':')

    return (
      <a
        href={`https://www.tbcdb.com/?item=${item.id}`}
        className={itemClass}
        rel={`${itemGems}&amp;${itemEnchant}&amp;${itemSet}`}
        onClick={e => e.preventDefault()}
        style={{ textDecoration: 'none' }}
      >
        {children}
      </a>
    )
  }

  return <>{children}</>
}
