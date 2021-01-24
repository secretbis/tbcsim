package data.model.socketbonus

import data.Constants

data class SocB3263 (
  override var id: Int = 3263,
  override var stat: Constants.StatType = Constants.StatType.CRIT_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
