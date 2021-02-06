package data.socketbonus

import data.Constants

data class SocB2859 (
  override var id: Int = 2859,
  override var stat: Constants.StatType = Constants.StatType.RESILIENCE_RATING,
  override var amount: Int = 3
) : SocketBonusRaw()
