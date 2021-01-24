package data.model.socketbonus

import data.Constants

data class SocB2880 (
  override var id: Int = 2880,
  override var stat: Constants.StatType = Constants.StatType.HIT_SPELL_RATING,
  override var amount: Int = 3
) : SocketBonusRaw()
