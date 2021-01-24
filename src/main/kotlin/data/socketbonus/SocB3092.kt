package data.socketbonus

import data.Constants

data class SocB3092 (
  override var id: Int = 3092,
  override var stat: Constants.StatType = Constants.StatType.CRIT_RATING,
  override var amount: Int = 3
) : SocketBonusRaw()
