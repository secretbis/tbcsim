// Kotlin codegen generates properties in the form of '_{property}_?\d*'
// These occasionally change, for reasons beyond the reach of my tiny primate brain
// This is fixed by @JsExport-ing them, but some things are not exportable - like enum classes
// This will find an own property that matches the Kotlin codegen pattern above
export function kprop(obj, property, defaultValue=null) {
  if(obj == null) return null;

  // Look for underscore-suffixed things first
  // This is because abstract classes with a default value will have the same prefix, but no suffix
  // e.g. Buff objects have an open var named 'hidden'.  JS Buff objects have both _hidden and _hidden_0, and _hidden_0 is the one we actually want
  let prop = Object.getOwnPropertyNames(obj).find(name => {
    return name.startsWith(`_${property}_`);
  })

  if(prop == null) {
    // Find the superclass default, no suffix
    prop = Object.getOwnPropertyNames(obj).find(name => {
      return name.startsWith(`_${property}`);
    })
  }

  // If we didn't find an own property, try everything
  if(prop == null) {
    for(let name in obj) {
      if(name.startsWith(`_${property}`)) {
        prop = name;
        break;
      }
    }
  }

  if(obj[prop] == null) {
    return defaultValue
  }

  return obj[prop];
}
