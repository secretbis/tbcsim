package data.model.socketbonus

import data.Constants

data class SocB2890 (
  override var id: Int = 2890,
  override var stat: Constants.StatType = Constants.StatType.SPIRIT,
  override var amount: Int = 4
) : SocketBonusRaw()
