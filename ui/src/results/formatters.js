export function toFixed(precision = 2) {
  return function(val) {
    if (typeof val === 'number') {
      return val.toLocaleString(undefined, {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
    return val
  }
}

export function toFixedPct(precision = 2) {
  return function(val) {
    const fixed = toFixed(precision)(val)
    return `${fixed}%`
  }
}

export function noop(val) {
  return val
}
