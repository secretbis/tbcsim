package data.socketbonus

import data.Constants

data class SocB2975 (
  override var id: Int = 2975,
  override var stat: Constants.StatType = Constants.StatType.BLOCK_VALUE,
  override var amount: Int = 5
) : SocketBonusRaw()
