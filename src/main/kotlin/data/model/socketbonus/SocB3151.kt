package data.model.socketbonus

import data.Constants

data class SocB3151 (
  override var id: Int = 3151,
  override var stat: Constants.StatType = Constants.StatType.SPELL_DAMAGE,
  override var amount: Int = 2,
  override var stat2: Constants.StatType? = Constants.StatType.SPELL_HEALING,
  override var amount2: Int = 4
) : SocketBonusRaw()
