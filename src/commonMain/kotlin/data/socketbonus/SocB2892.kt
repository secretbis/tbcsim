package data.socketbonus

import data.Constants

data class SocB2892 (
  override var id: Int = 2892,
  override var stat: Constants.StatType = Constants.StatType.STRENGTH,
  override var amount: Int = 4
) : SocketBonusRaw()
