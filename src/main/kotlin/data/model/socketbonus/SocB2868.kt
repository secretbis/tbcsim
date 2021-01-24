package data.model.socketbonus

import data.Constants

data class SocB2868 (
  override var id: Int = 2868,
  override var stat: Constants.StatType = Constants.StatType.STAMINA,
  override var amount: Int = 6
) : SocketBonusRaw()
