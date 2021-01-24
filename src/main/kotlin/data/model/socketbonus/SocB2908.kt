package data.model.socketbonus

import data.Constants

data class SocB2908 (
  override var id: Int = 2908,
  override var stat: Constants.StatType = Constants.StatType.HIT_SPELL_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
