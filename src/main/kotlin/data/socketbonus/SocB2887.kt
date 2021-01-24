package data.socketbonus

import data.Constants

data class SocB2887 (
  override var id: Int = 2887,
  override var stat: Constants.StatType = Constants.StatType.CRIT_RATING,
  override var amount: Int = 3
) : SocketBonusRaw()
