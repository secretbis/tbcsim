package data.model.socketbonus

import data.Constants

data class SocB2951 (
  override var id: Int = 2951,
  override var stat: Constants.StatType = Constants.StatType.CRIT_SPELL_RATING,
  override var amount: Int = 4
) : SocketBonusRaw()
