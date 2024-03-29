package data.socketbonus

import character.Stats
import data.model.SocketBonus

object SocketBonuses {
    fun byId(id: Int): SocketBonus? {
        val sb = allSocketBonusesRaw.find { it.id == id }
        return if(sb == null) {
            null
        } else {
            val stats = Stats()
            stats.addByStatType(sb.stat, sb.amount)
            if(sb.stat2 != null) {
                stats.addByStatType(sb.stat2!!, sb.amount2)
            }
            SocketBonus(stats)
        }
    }

    // TODO: Map-ify this, finding every time is inefficient
    val allSocketBonusesRaw = listOf(
        SocB76(),
        SocB90(),
        SocB113(),
        SocB2856(),
        SocB2857(),
        SocB2859(),
        SocB2860(),
        SocB2861(),
        SocB2862(),
        SocB2863(),
        SocB2864(),
        SocB2865(),
        SocB2866(),
        SocB2867(),
        SocB2868(),
        SocB2869(),
        SocB2870(),
        SocB2871(),
        SocB2872(),
        SocB2873(),
        SocB2874(),
        SocB2875(),
        SocB2876(),
        SocB2877(),
        SocB2878(),
        SocB2879(),
        SocB2880(),
        SocB2881(),
        SocB2882(),
        SocB2884(),
        SocB2885(),
        SocB2887(),
        SocB2888(),
        SocB2889(),
        SocB2890(),
        SocB2892(),
        SocB2893(),
        SocB2895(),
        SocB2900(),
        SocB2902(),
        SocB2908(),
        SocB2909(),
        SocB2925(),
        SocB2926(),
        SocB2927(),
        SocB2932(),
        SocB2936(),
        SocB2941(),
        SocB2951(),
        SocB2952(),
        SocB2953(),
        SocB2972(),
        SocB2973(),
        SocB2974(),
        SocB2975(),
        SocB2976(),
        SocB3015(),
        SocB3016(),
        SocB3017(),
        SocB3092(),
        SocB3094(),
        SocB3097(),
        SocB3098(),
        SocB3114(),
        SocB3149(),
        SocB3151(),
        SocB3152(),
        SocB3153(),
        SocB3164(),
        SocB3205(),
        SocB3263(),
        SocB3267(),
    )
}
