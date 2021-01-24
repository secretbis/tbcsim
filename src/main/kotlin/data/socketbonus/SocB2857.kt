package data.socketbonus

import data.Constants

data class SocB2857 (
  override var id: Int = 2857,
  override var stat: Constants.StatType = Constants.StatType.CRIT_RATING,
  override var amount: Int = 2
) : SocketBonusRaw()
