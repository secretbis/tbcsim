package data.socketbonus

import data.Constants

data class SocB2882 (
  override var id: Int = 2882,
  override var stat: Constants.StatType = Constants.StatType.STAMINA,
  override var amount: Int = 6
) : SocketBonusRaw()
