package data.socketbonus

import data.Constants

data class SocB2879 (
  override var id: Int = 2879,
  override var stat: Constants.StatType = Constants.StatType.STRENGTH,
  override var amount: Int = 3
) : SocketBonusRaw()
