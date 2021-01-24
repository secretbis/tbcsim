package data.socketbonus

import data.Constants

data class SocB2888 (
  override var id: Int = 2888,
  override var stat: Constants.StatType = Constants.StatType.BLOCK_VALUE,
  override var amount: Int = 6
) : SocketBonusRaw()
