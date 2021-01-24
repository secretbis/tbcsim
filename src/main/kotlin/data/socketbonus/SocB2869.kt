package data.socketbonus

import data.Constants

data class SocB2869 (
  override var id: Int = 2869,
  override var stat: Constants.StatType = Constants.StatType.INTELLECT,
  override var amount: Int = 4
) : SocketBonusRaw()
