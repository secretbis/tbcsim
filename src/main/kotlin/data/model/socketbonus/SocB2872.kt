package data.model.socketbonus

import data.Constants

data class SocB2872 (
  override var id: Int = 2872,
  override var stat: Constants.StatType = Constants.StatType.SPELL_DAMAGE,
  override var amount: Int = 3,
  override var stat2: Constants.StatType? = Constants.StatType.SPELL_HEALING,
  override var amount2: Int = 9
) : SocketBonusRaw()
