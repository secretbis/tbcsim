package data.socketbonus

import data.Constants

data class SocB2973 (
  override var id: Int = 2973,
  override var stat: Constants.StatType = Constants.StatType.ATTACK_POWER,
  override var amount: Int = 6
) : SocketBonusRaw()
