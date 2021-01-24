package data.model.socketbonus

import data.Constants

data class SocB2925 (
  override var id: Int = 2925,
  override var stat: Constants.StatType = Constants.StatType.STAMINA,
  override var amount: Int = 3
) : SocketBonusRaw()
