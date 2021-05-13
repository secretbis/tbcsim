export function linkedHashMapKeys(lhm) {
  const found = []

  function _lhm(node) {
    if(node) {
      const key = node._key_0
      if(!found.includes(key)) {
        found.push(key)
        _lhm(node._next_5)
      }
    }
  }

  if(lhm) {
    _lhm(lhm._head)
  }

  return found
}
