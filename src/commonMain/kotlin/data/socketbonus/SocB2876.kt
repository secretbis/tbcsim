package data.socketbonus

import data.Constants

data class SocB2876 (
  override var id: Int = 2876,
  override var stat: Constants.StatType = Constants.StatType.DODGE_RATING,
  override var amount: Int = 3
) : SocketBonusRaw()
