package data.model.socketbonus

import data.Constants

data class SocB3149 (
  override var id: Int = 3149,
  override var stat: Constants.StatType = Constants.StatType.AGILITY,
  override var amount: Int = 2
) : SocketBonusRaw()
