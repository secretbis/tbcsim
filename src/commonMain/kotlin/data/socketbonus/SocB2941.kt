package data.socketbonus

import data.Constants

data class SocB2941 (
  override var id: Int = 2941,
  override var stat: Constants.StatType = Constants.StatType.HIT_RATING,
  override var amount: Int = 2
) : SocketBonusRaw()
