package data.model.socketbonus

import data.Constants

data class SocB2875 (
  override var id: Int = 2875,
  override var stat: Constants.StatType = Constants.StatType.CRIT_SPELL_RATING,
  override var amount: Int = 3
) : SocketBonusRaw()
