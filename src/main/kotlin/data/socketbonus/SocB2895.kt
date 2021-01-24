package data.socketbonus

import data.Constants

data class SocB2895 (
  override var id: Int = 2895,
  override var stat: Constants.StatType = Constants.StatType.STAMINA,
  override var amount: Int = 4
) : SocketBonusRaw()
