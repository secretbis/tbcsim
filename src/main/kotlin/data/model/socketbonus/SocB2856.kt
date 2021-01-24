package data.model.socketbonus

import data.Constants

data class SocB2856 (
  override var id: Int = 2856,
  override var stat: Constants.StatType = Constants.StatType.RESILIENCE_RATING,
  override var amount: Int = 4
) : SocketBonusRaw ()
