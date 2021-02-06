package data.socketbonus

import data.Constants

data class SocB90 (
  override var id: Int = 90,
  override var stat: Constants.StatType = Constants.StatType.AGILITY,
  override var amount: Int = 4
) : SocketBonusRaw ()
