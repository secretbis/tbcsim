package data.model.socketbonus

import data.Constants

data class SocB3015 (
  override var id: Int = 3015,
  override var stat: Constants.StatType = Constants.StatType.STRENGTH,
  override var amount: Int = 2
) : SocketBonusRaw()
