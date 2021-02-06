package data.socketbonus

import data.Constants

data class SocB2909 (
  override var id: Int = 2909,
  override var stat: Constants.StatType = Constants.StatType.HIT_SPELL_RATING,
  override var amount: Int = 2
) : SocketBonusRaw()
