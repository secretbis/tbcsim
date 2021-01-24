package data.socketbonus

import data.Constants

data class SocB3097 (
  override var id: Int = 3097,
  override var stat: Constants.StatType = Constants.StatType.SPIRIT,
  override var amount: Int = 2
) : SocketBonusRaw()
