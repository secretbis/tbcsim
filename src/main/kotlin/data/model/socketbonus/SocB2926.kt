package data.model.socketbonus

import data.Constants

data class SocB2926 (
  override var id: Int = 2926,
  override var stat: Constants.StatType = Constants.StatType.DODGE_RATING,
  override var amount: Int = 2
) : SocketBonusRaw()
