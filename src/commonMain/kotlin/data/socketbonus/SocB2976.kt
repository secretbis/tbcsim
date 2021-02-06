package data.socketbonus

import data.Constants

data class SocB2976 (
  override var id: Int = 2976,
  override var stat: Constants.StatType = Constants.StatType.DEFENSE_SKILL_RATING,
  override var amount: Int = 2
) : SocketBonusRaw()
