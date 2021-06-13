import * as tbcsim from 'tbcsim';

// These are somewhat different from the desktop version, due to JS performance constraints
const simDefaults = {
  durationSeconds: tbcsim.sim.SimDefaults.durationMs / 1000,
  durationVariabilitySeconds: tbcsim.sim.SimDefaults.durationVaribilityMs / 1000,
  stepMs: 10,
  latencyMs: tbcsim.sim.SimDefaults.latencyMs,
  iterations: 100,
  targetLevel: tbcsim.sim.SimDefaults.targetLevel,
  targetArmor: tbcsim.sim.SimDefaults.targetArmor,
  // allowParryAndBlock: tbcsim.sim.SimDefaults.allowParryAndBlock,
  showHiddenBuffs: tbcsim.sim.SimDefaults.showHiddenBuffs
};

export default simDefaults;
