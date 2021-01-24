package data.model.socketbonus

import data.Constants

data class SocB113 (
  override var id: Int = 113,
  override var stat: Constants.StatType = Constants.StatType.DEFENSE_SKILL_RATING,
  override var amount: Int = 4
) : SocketBonusRaw ()
