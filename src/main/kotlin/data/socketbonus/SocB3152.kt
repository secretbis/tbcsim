package data.socketbonus

import data.Constants

data class SocB3152 (
  override var id: Int = 3152,
  override var stat: Constants.StatType = Constants.StatType.CRIT_SPELL_RATING,
  override var amount: Int = 2
) : SocketBonusRaw()
