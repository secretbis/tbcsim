package data.socketbonus

import data.Constants

data class SocB2867 (
  override var id: Int = 2867,
  override var stat: Constants.StatType = Constants.StatType.RESILIENCE_RATING,
  override var amount: Int = 2
) : SocketBonusRaw()
