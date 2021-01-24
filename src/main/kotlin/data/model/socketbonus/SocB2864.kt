package data.model.socketbonus

import data.Constants

data class SocB2864 (
  override var id: Int = 2864,
  override var stat: Constants.StatType = Constants.StatType.CRIT_SPELL_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
