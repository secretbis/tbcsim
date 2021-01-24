package data.model.socketbonus

import data.Constants

data class SocB3205 (
  override var id: Int = 3205,
  override var stat: Constants.StatType = Constants.StatType.CRIT_RATING,
  override var amount: Int = 3
) : SocketBonusRaw()
