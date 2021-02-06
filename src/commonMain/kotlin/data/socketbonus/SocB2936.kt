package data.socketbonus

import data.Constants

data class SocB2936 (
  override var id: Int = 2936,
  override var stat: Constants.StatType = Constants.StatType.ATTACK_POWER,
  override var amount: Int = 8
) : SocketBonusRaw()
