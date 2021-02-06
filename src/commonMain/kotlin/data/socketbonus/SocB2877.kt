package data.socketbonus

import data.Constants

data class SocB2877 (
  override var id: Int = 2877,
  override var stat: Constants.StatType = Constants.StatType.AGILITY,
  override var amount: Int = 4
) : SocketBonusRaw()
