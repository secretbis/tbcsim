package data.socketbonus

import data.Constants

data class SocB2873 (
  override var id: Int = 2873,
  override var stat: Constants.StatType = Constants.StatType.HIT_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
