package data.model.socketbonus

import data.Constants

data class SocB2900 (
  override var id: Int = 2900,
  override var stat: Constants.StatType = Constants.StatType.SPELL_DAMAGE,
  override var amount: Int = 4,
  override var stat2: Constants.StatType? = Constants.StatType.SPELL_HEALING,
  override var amount2: Int = 4
) : SocketBonusRaw()
