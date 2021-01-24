package data.model.socketbonus

import data.Constants

data class SocB76 (
  override var id: Int = 76,
  override var stat: Constants.StatType = Constants.StatType.AGILITY,
  override var amount: Int = 3
) : SocketBonusRaw ()
