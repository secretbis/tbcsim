package data.model.socketbonus

import data.Constants

data class SocB2865 (
  override var id: Int = 2865,
  override var stat: Constants.StatType = Constants.StatType.MANA_PER_5_SECONDS,
  override var amount: Int = 2
) : SocketBonusRaw()
