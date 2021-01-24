package data.model.socketbonus

import data.Constants

data class SocB2861 (
  override var id: Int = 2861,
  override var stat: Constants.StatType = Constants.StatType.DEFENSE_SKILL_RATING,
  override var amount: Int = 3
) : SocketBonusRaw()
