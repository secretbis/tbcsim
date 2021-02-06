package data.socketbonus

import data.Constants

data class SocB2952 (
  override var id: Int = 2952,
  override var stat: Constants.StatType = Constants.StatType.CRIT_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
