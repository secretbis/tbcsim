package data.socketbonus

import data.Constants

data class SocB2860 (
  override var id: Int = 2860,
  override var stat: Constants.StatType = Constants.StatType.HIT_RATING,
  override var amount: Int = 3
) : SocketBonusRaw()
