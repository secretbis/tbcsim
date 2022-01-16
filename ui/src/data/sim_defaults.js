import * as tbcsim from 'tbcsim';

// These are somewhat different from the desktop version, due to JS performance constraints
const simDefaults = {
  durationSeconds: tbcsim.sim.SimDefaults.durationMs / 1000,
  durationVariabilitySeconds: tbcsim.sim.SimDefaults.durationVariabilityMs / 1000,
  stepMs: 10,
  latencyMs: tbcsim.sim.SimDefaults.latencyMs,
  iterations: 100,

  targetProfile: tbcsim.sim.SimDefaults.targetProfile,
  targetLevel: tbcsim.sim.SimDefaults.targetLevel,
  targetArmor: tbcsim.sim.SimDefaults.targetArmor,
  targetType: tbcsim.sim.SimDefaults.targetType || 3,
  targetActive: tbcsim.sim.SimDefaults.targetActive,
  targetAutoAttackSpeedMs: tbcsim.sim.SimDefaults.targetAutoAttackSpeedMs,
  targetWeaponPower: tbcsim.sim.SimDefaults.targetWeaponPower,
  targetDualWield: tbcsim.sim.SimDefaults.targetDualWield,

  // allowParryAndBlock: tbcsim.sim.SimDefaults.allowParryAndBlock,
  showHiddenBuffs: tbcsim.sim.SimDefaults.showHiddenBuffs
};

export default simDefaults;
