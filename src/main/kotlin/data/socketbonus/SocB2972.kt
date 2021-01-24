package data.socketbonus

import data.Constants

data class SocB2972 (
  override var id: Int = 2972,
  override var stat: Constants.StatType = Constants.StatType.BLOCK_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
