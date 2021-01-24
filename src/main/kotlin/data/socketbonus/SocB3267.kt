package data.socketbonus

import data.Constants

data class SocB3267 (
  override var id: Int = 3267,
  override var stat: Constants.StatType = Constants.StatType.HASTE_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
