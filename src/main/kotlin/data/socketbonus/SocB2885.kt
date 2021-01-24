package data.socketbonus

import data.Constants

data class SocB2885 (
  override var id: Int = 2885,
  override var stat: Constants.StatType = Constants.StatType.CRIT_RATING,
  override var amount: Int = 2
) : SocketBonusRaw()
