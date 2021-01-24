package data.model.socketbonus

import data.Constants

data class SocB2863 (
  override var id: Int = 2863,
  override var stat: Constants.StatType = Constants.StatType.INTELLECT,
  override var amount: Int = 3
) : SocketBonusRaw()
