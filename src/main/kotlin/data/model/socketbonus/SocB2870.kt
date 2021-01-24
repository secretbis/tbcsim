package data.model.socketbonus

import data.Constants

data class SocB2870 (
  override var id: Int = 2870,
  override var stat: Constants.StatType = Constants.StatType.PARRY_RATING,
  override var amount: Int = 3
) : SocketBonusRaw()
