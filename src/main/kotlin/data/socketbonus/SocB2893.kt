package data.socketbonus

import data.Constants

data class SocB2893 (
  override var id: Int = 2893,
  override var stat: Constants.StatType = Constants.StatType.AGILITY,
  override var amount: Int = 3
) : SocketBonusRaw()
