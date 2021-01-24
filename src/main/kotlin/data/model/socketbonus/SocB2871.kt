package data.model.socketbonus

import data.Constants

data class SocB2871 (
  override var id: Int = 2871,
  override var stat: Constants.StatType = Constants.StatType.DODGE_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
