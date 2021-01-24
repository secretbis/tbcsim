package data.socketbonus

import data.Constants

data class SocB2878 (
  override var id: Int = 2878,
  override var stat: Constants.StatType = Constants.StatType.RESILIENCE_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
