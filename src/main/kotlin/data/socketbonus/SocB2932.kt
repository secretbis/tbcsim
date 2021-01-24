package data.socketbonus

import data.Constants

data class SocB2932 (
  override var id: Int = 2932,
  override var stat: Constants.StatType = Constants.StatType.DEFENSE_SKILL_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
