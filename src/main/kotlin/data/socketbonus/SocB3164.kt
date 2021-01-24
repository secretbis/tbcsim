package data.socketbonus

import data.Constants

data class SocB3164 (
  override var id: Int = 3164,
  override var stat: Constants.StatType = Constants.StatType.STAMINA,
  override var amount: Int = 3
) : SocketBonusRaw()
