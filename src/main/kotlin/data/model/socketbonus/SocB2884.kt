package data.model.socketbonus

import data.Constants

data class SocB2884 (
  override var id: Int = 2884,
  override var stat: Constants.StatType = Constants.StatType.CRIT_SPELL_RATING,
  override var amount: Int = 2
) : SocketBonusRaw()
