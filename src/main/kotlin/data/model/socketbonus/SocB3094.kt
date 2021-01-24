package data.model.socketbonus

import data.Constants

data class SocB3094 (
  override var id: Int = 3094,
  override var stat: Constants.StatType = Constants.StatType.SPIRIT,
  override var amount: Int = 2
) : SocketBonusRaw()
