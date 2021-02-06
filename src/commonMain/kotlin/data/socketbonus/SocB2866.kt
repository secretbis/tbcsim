package data.socketbonus

import data.Constants

data class SocB2866 (
  override var id: Int = 2866,
  override var stat: Constants.StatType = Constants.StatType.SPIRIT,
  override var amount: Int = 3
) : SocketBonusRaw()
