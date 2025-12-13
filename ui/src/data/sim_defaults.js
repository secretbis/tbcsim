import * as tbcsim from 'tbcsim';

// These are somewhat different from the desktop version, due to JS performance constraints
const baseDefaults = tbcsim.SimDefaults.getInstance();
const simDefaults = {
  durationSeconds: baseDefaults.durationMs / 1000,
  durationVariabilitySeconds: baseDefaults.durationVaribilityMs / 1000,
  stepMs: 10,
  latencyMs: baseDefaults.latencyMs,
  iterations: 100,
  targetLevel: baseDefaults.targetLevel,
  targetArmor: baseDefaults.targetArmor,
  // allowParryAndBlock: baseDefaults.allowParryAndBlock,
  showHiddenBuffs: baseDefaults.showHiddenBuffs,
  targetType: baseDefaults.targetType || 3
};

export default simDefaults;
