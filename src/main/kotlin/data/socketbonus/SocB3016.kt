package data.socketbonus

import data.Constants

data class SocB3016 (
  override var id: Int = 3016,
  override var stat: Constants.StatType = Constants.StatType.INTELLECT,
  override var amount: Int = 2
) : SocketBonusRaw()
