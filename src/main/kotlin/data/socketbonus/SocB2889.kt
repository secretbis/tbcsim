package data.socketbonus

import data.Constants

data class SocB2889 (
  override var id: Int = 2889,
  override var stat: Constants.StatType = Constants.StatType.SPELL_DAMAGE,
  override var amount: Int = 5,
  override var stat2: Constants.StatType? = Constants.StatType.SPELL_HEALING,
  override var amount2: Int = 5
) : SocketBonusRaw()
