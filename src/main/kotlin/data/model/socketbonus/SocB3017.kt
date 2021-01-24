package data.model.socketbonus

import data.Constants

data class SocB3017 (
  override var id: Int = 3017,
  override var stat: Constants.StatType = Constants.StatType.BLOCK_VALUE,
  override var amount: Int = 3
) : SocketBonusRaw()
