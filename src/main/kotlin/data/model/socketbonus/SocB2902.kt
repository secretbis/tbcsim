package data.model.socketbonus

import data.Constants

data class SocB2902 (
  override var id: Int = 2902,
  override var stat: Constants.StatType = Constants.StatType.CRIT_RATING,
  override var amount: Int = 2
) : SocketBonusRaw()
