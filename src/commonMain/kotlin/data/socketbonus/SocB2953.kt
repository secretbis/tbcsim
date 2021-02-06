package data.socketbonus

import data.Constants

data class SocB2953 (
  override var id: Int = 2953,
  override var stat: Constants.StatType = Constants.StatType.SPELL_DAMAGE,
  override var amount: Int = 2
) : SocketBonusRaw()
