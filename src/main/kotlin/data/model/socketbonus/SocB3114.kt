package data.model.socketbonus

import data.Constants

data class SocB3114 (
  override var id: Int = 3114,
  override var stat: Constants.StatType = Constants.StatType.ATTACK_POWER,
  override var amount: Int = 4
) : SocketBonusRaw()
