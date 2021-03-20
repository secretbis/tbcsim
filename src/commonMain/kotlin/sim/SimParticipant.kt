package sim

import character.*
import character.auto.MeleeBase
import character.auto.MeleeMainHand
import character.auto.MeleeOffHand
import data.model.Item
import mechanics.Rating
import mu.KotlinLogging
import sim.rotation.Criterion
import sim.rotation.Rotation
import sim.rotation.Rule
import kotlin.js.JsExport
import kotlin.math.floor

@JsExport
open class SimParticipant(val character: Character, val rotation: Rotation, val sim: SimIteration) {
    val logger = KotlinLogging.logger {}

    var stats: Stats = Stats()
    lateinit var resource: Resource

    var mhAutoAttack: MeleeBase? = null
    var ohAutoAttack: MeleeBase? = null

    var buffs: MutableMap<String, Buff> = mutableMapOf()
    var debuffs: MutableMap<String, Debuff> = mutableMapOf()

    // Track debuff expirations per tick, and per buff
    // If these get out of sync, sadness will probably ensue, and simstats will be confused
    val buffExpirations: MutableMap<Int, MutableSet<Buff>> = mutableMapOf()
    val buffExpirationTick: MutableMap<String, Int> = mutableMapOf()
    val debuffExpirations: MutableMap<Int, MutableSet<Debuff>> = mutableMapOf()
    val debuffExpirationTick: MutableMap<String, Int> = mutableMapOf()

    // Buffs need a place to store state per iteration
    // Store individual data per instance and store shared data per-string (generally the buff name)
    val buffState: MutableMap<String, Buff.State> = mutableMapOf()
    val debuffState: MutableMap<String, Buff.State> = mutableMapOf()
    val abilityState: MutableMap<String, Ability.State> = mutableMapOf()
    val sharedAbilityState: MutableMap<String, Ability.State> = mutableMapOf()
    val procState: MutableMap<Proc, Proc.State> = mutableMapOf()
    val rotationState: MutableMap<Criterion.Type, Criterion.State> = mutableMapOf()

    // GCD/casting state
    var gcdEndMs: Int = 0
    var castEndMs: Int = 0
    var castingRule: Rule? = null
    var mainHandAutoReplacement: Ability? = null

    // Events
    var events: MutableList<Event> = mutableListOf()

    fun init(): SimParticipant {
        // Add auto-attack, if allowed
        if (rotation.autoAttack) {
            if (hasMainHandWeapon()) {
                mhAutoAttack = MeleeMainHand()
            }
            if (hasOffHandWeapon()) {
                ohAutoAttack = MeleeOffHand()
            }
        }

        // Collect buffs from class, talents, gear, and etc
        character.race.buffs(this).forEach { addBuff(it) }
        character.klass.buffs.forEach { addBuff(it) }
        character.klass.talents.filter { it.value.currentRank > 0 }.forEach {
            it.value.buffs(this).forEach { buff -> addBuff(buff) }
        }
        character.gear.buffs().forEach { addBuff(it) }
        rotation.combat(this).forEach { it.buffs(this).forEach { buff -> addBuff(buff) } }

        // Compute initial stats
        recomputeStats()

        // Initialize our subject resource
        resource = Resource(this)

        // Cast any spells flagged in the rotation as precombat
        rotation.castAllRaidBuffs(this)
        rotation.castAllPrecombat(this)

        // Recompute after precombat casts
        recomputeStats()

        return this
    }

    fun onGcd(): Boolean {
        return sim.elapsedTimeMs < gcdEndMs
    }

    fun isCasting(): Boolean {
        return sim.elapsedTimeMs < castEndMs
    }

    fun recomputeStats() {
        stats = Stats()
            .add(character.klass.baseStats)
            .add(character.race.baseStats)
            .add(character.gear.totalStats())
            .let {
                (buffs.values + debuffs.values).forEach { buff ->
                    val stats = buff.modifyStats(this)
                    if(stats != null) {
                        it.add(stats)
                    }
                }
                it
            }
    }

    fun tick() {
        // Find and cast next rotation ability
        if(!isCasting()) {
            // If we are not casting, and have an ability queued up, actually cast it
            if(castingRule != null) {
                castingRule!!.ability.beforeCast(this)
                castingRule!!.ability.cast(this)
                castingRule!!.ability.afterCast(this)

                // Log cast event
                logEvent(
                    Event(
                        eventType = Event.Type.SPELL_CAST,
                        abilityName = castingRule!!.ability.name,
                        target = sim.target
                    )
                )

                // Reset casting state
                castingRule = null
            } else {
                val rotationRule = rotation.next(this, onGcd())
                val rotationAbility = rotationRule?.ability
                if (rotationAbility != null) {
                    // Set next cast times, and add latency if configured
                    castingRule = rotationRule
                    gcdEndMs = sim.elapsedTimeMs + rotationAbility.gcdMs(this) + sim.opts.latencyMs
                    castEndMs = sim.elapsedTimeMs + rotationAbility.castTimeMs(this) + sim.opts.latencyMs
                }
            }

            // Double check casting, since we could have just started
            // Do auto attacks
            if (!isCasting()) {
                if (mhAutoAttack?.available(this) == true) {
                    // Check to see if we have a replacement ability
                    // Be sure to double check the cost, since our resource may have changed since we requested the replacement
                    if(mhAutoAttack is MeleeMainHand && mainHandAutoReplacement != null) {
                        // If we can cast it, do so
                        if(mainHandAutoReplacement?.available(this) == true) {
                            mainHandAutoReplacement!!.cast(this)
                        } else mhAutoAttack?.cast(this)

                        // Reset our requested ability, regardless if we were able to use it or not
                        mainHandAutoReplacement = null

                        // Mark our MH auto attack as having occurred
                        val mhState = mhAutoAttack?.state(this) as MeleeBase.AutoAttackState?
                        mhState?.lastAttackTimeMs = sim.elapsedTimeMs
                    } else mhAutoAttack?.cast(this)
                } else if (ohAutoAttack?.available(this) == true) {
                    ohAutoAttack?.cast(this)
                }
            }
        }
    }

    fun replaceNextMainHandAutoAttack(ability: Ability) {
        this.mainHandAutoReplacement = ability
    }

    // Buffs
    fun addBuff(buff: Buff) {
        buff.refresh(this)

        // If this buff stacks, track stacks
        val stacks = if(buff.maxStacks > 0) {
            buffState[buff.name]?.currentStacks ?: 0
        } else 0

        // Set expiration tick
        // Remove the old expiration
        if(buff.durationMs != -1) {
            val oldTick = buffExpirationTick[buff.name]
            buffExpirations[oldTick]?.removeAll { it.name == buff.name }

            // Find the new expiration, and store that in both places
            val newTick = sim.getExpirationTick(buff)
            buffExpirationTick[buff.name] = newTick

            val expirationSet = buffExpirations.getOrPut(newTick, { mutableSetOf() })
            expirationSet.add(buff)
        }

        // If this is a new buff, add it
        val exists = buffs[buff.name] != null
        if(!exists) {
            // If this buff is mutex with others, remove any existing with that class
            if(!buff.mutex.contains(Mutex.NONE)) {
                // A buff should be removed if it matches any of the incoming buff's mutex categories
                val toRemove = buffs.values.filter { existing -> buff.mutex.any { existing.mutex.contains(it) } }
                toRemove.forEach {
                    it.reset(this)
                    removeBuffs(listOf(it))
                }
            }

            buffs[buff.name] = buff
            logEvent(Event(
                eventType = Event.Type.BUFF_START,
                buff = buff,
                buffStacks = stacks
            ))

            // Always recompute after adding a buff
            recomputeStats()
        } else {
            logEvent(Event(
                eventType = Event.Type.BUFF_REFRESH,
                buff = buff,
                buffStacks = stacks
            ))

            // If a buff is stackable, then recompute on a refresh as well
            if(stacks > 0) {
                recomputeStats()
            }
        }
    }

    fun consumeBuff(buff: Buff) {
        val state = buffState[buff.name]
        if(state != null) {
            if(buff.maxCharges >= 1) {
                state.currentCharges -= 1

                logEvent(
                    Event(
                        eventType = Event.Type.BUFF_CHARGE_CONSUMED,
                        buff = buff,
                        buffStacks = state.currentStacks
                    )
                )

                // Remove this if fully consumed
                if(state.currentCharges == 0) {
                    removeBuffs(listOf(buff))
                }

            } else {
                removeBuffs(listOf(buff))
            }
        }
    }

    fun pruneBuffs() {
        val toRemove = buffExpirations[sim.tickNum]
        if(toRemove?.isEmpty() == false) {
            removeBuffs(toRemove.toList())
        }
    }

    private fun removeBuffs(buffsList: List<Buff>) {
        buffsList.forEach {
            buffs.remove(it.name)
            buffState.remove(it.name)

            val expirationTick = buffExpirationTick[it.name]
            if(expirationTick != null) {
                buffExpirations[expirationTick]?.removeAll { it2 -> it2.name == it.name }
            }

            it.reset(this)

            logEvent(Event(
                eventType = Event.Type.BUFF_END,
                buff = it
            ))
        }

        recomputeStats()
    }

     // Debuffs
    fun addDebuff(debuff: Debuff) {
        debuff.refresh(this)

        // If this debuff stacks, track stacks
        val stacks = if(debuff.maxStacks > 0) {
            debuffState[debuff.name]?.currentStacks ?: 0
        } else 0

        // Set expiration tick
        // Remove the old expiration
         if(debuff.durationMs != -1) {
             val oldTick = debuffExpirationTick[debuff.name]
             debuffExpirations[oldTick]?.removeAll { it.name == debuff.name }

             // Find the new expiration, and store that in both places
             val newTick = sim.getExpirationTick(debuff)
             debuffExpirationTick[debuff.name] = newTick

             val expirationSet = debuffExpirations.getOrPut(newTick, { mutableSetOf() })
             expirationSet.add(debuff)
         }

        // If this is a new debuff, add it
        val exists = debuffs[debuff.name] != null
        if(!exists) {
            debuffs[debuff.name] = debuff
            logEvent(Event(
                eventType = Event.Type.DEBUFF_START,
                buff = debuff,
                buffStacks = stacks,
                target = sim.target
            ))

            // Always recompute after adding a debuff
            recomputeStats()
        } else {
            logEvent(Event(
                eventType = Event.Type.DEBUFF_REFRESH,
                buff = debuff,
                buffStacks = stacks,
                target = sim.target
            ))

            // If a debuff is stackable, then recompute on a refresh as well
            if(stacks > 0) {
                recomputeStats()
            }
        }
    }

    fun consumeDebuff(debuff: Debuff) {
        val state = debuffState[debuff.name]
        if(state != null) {
            if(debuff.maxCharges >= 1) {
                state.currentCharges -= 1

                logEvent(
                    Event(
                        eventType = Event.Type.DEBUFF_CHARGE_CONSUMED,
                        buff = debuff,
                        buffStacks = state.currentStacks,
                        target = sim.target
                    )
                )

                // Remove this if fully consumed
                if(state.currentCharges == 0) {
                    removeDebuffs(listOf(debuff))
                }
            } else {
                removeDebuffs(listOf(debuff))
            }
        }
    }

    fun pruneDebuffs() {
        val toRemove = debuffExpirations[sim.tickNum]
        if(toRemove?.isEmpty() == false) {
            removeDebuffs(toRemove.toList())
        }
    }

    private fun removeDebuffs(debuffsList: List<Debuff>) {
        debuffsList.forEach {
            debuffs.remove(it.name)
            debuffState.remove(it.name)

            val expirationTick = debuffExpirationTick[it.name]
            if(expirationTick != null) {
                debuffExpirations[expirationTick]?.removeAll { it2 -> it2.name == it.name }
            }

            it.reset(this)
            logEvent(Event(
                eventType = Event.Type.DEBUFF_END,
                buff = it,
                target = sim.target
            ))
        }

        recomputeStats()
    }

    // Resource
    fun addResource(amount: Int, type: Resource.Type, abilityName: String) {
        if(resource.type == type) {
            resource.add(amount)

            logEvent(Event(
                eventType = Event.Type.RESOURCE_CHANGED,
                amount = resource.currentAmount.toDouble(),
                delta = amount.toDouble(),
                amountPct = resource.currentAmount / resource.maxAmount.toDouble() * 100.0,
                abilityName = abilityName
            ))
        } else {
            logger.warn { "Attempted to add resource type $type but subject resource is ${resource.type}" }
        }
    }

    fun subtractResource(amount: Int, type: Resource.Type, abilityName: String) {
        if(resource.type == type) {
            resource.subtract(amount)

            logEvent(Event(
                eventType = Event.Type.RESOURCE_CHANGED,
                amount = resource.currentAmount.toDouble(),
                delta = -1 * amount.toDouble(),
                amountPct = resource.currentAmount / resource.maxAmount.toDouble() * 100.0,
                abilityName = abilityName
            ))
        } else {
            logger.warn { "Attempted to subtract resource type $type but subject resource is ${resource.type}" }
        }
    }

    fun fireProc(triggers: List<Proc.Trigger>, items: List<Item>?, ability: Ability?, event: Event?) {
        // Collect fireable procs
        val allProcs: MutableSet<Proc> = mutableSetOf()
        for(trigger in triggers) {
            // Get procs from active buffs
            (buffs.values + debuffs.values).forEach { buff ->
                buff.procs(this).filter { proc -> proc.triggers.contains(trigger) }.forEach {
                    allProcs.add(it)
                }
            }
        }

        // Fire all found procs
        allProcs.forEach {
            if(it.shouldProc(this, items, ability, event)) {
                it.proc(this, items, ability, event)
                it.afterProc(this)
            }
        }
    }

    fun cleanup() {
        // Log end for all buffs/debuffs
        buffs.values.forEach { buff ->
            logEvent(Event(
                eventType = Event.Type.BUFF_END,
                buff = buff
           ))
        }

        debuffs.values.forEach { debuff ->
            logEvent(Event(
                eventType = Event.Type.DEBUFF_END,
                buff = debuff
            ))
        }
    }

    fun logEvent(event: Event) {
        // Auto-set tick and time if not specified
        if(event.tick == -1) {
            event.tick = sim.tickNum
        }

        if(event.timeMs == -1) {
            event.timeMs = sim.elapsedTimeMs
        }

        // Auto-set target if we can
        if(event.eventType == Event.Type.DAMAGE) {
            event.target = event.target ?: sim.target
        }

        logger.trace { "Got event: ${event.abilityName} - ${event.tick} (${event.tick * sim.opts.stepMs}ms) - ${event.eventType} - ${event.result} - ${event.amount}" }

        events.add(event)
    }

    // Computed stats
    fun hasMainHandWeapon(): Boolean {
        return character.gear.mainHand.id != -1
    }

    fun hasOffHandWeapon(): Boolean {
        return character.gear.offHand.id != -1
    }

    fun isDualWielding(): Boolean {
        return character.klass.canDualWield && hasMainHandWeapon() && hasOffHandWeapon()
    }

    fun weaponSpeed(item: Item): Double {
        return (item.speed / meleeHasteMultiplier()).coerceAtLeast(0.01)
    }

    fun strength(): Int {
        return (stats.strength.coerceAtLeast(0) * stats.strengthMultiplier).toInt()
    }

    fun agility(): Int {
        return (stats.agility.coerceAtLeast(0) * stats.agilityMultiplier).toInt()
    }

    fun intellect(): Int {
        return (stats.intellect.coerceAtLeast(0) * stats.intellectMultiplier).toInt()
    }

    fun spirit(): Int {
        return (stats.spirit.coerceAtLeast(0) * stats.spiritMultiplier).toInt()
    }

    fun stamina(): Int {
        return (stats.stamina.coerceAtLeast(0) * stats.staminaMultiplier).toInt()
    }

    fun armor(): Int {
        return (2 * agility()) + (stats.armor.coerceAtLeast(0) * stats.armorMultiplier).toInt()
    }

    fun attackPower(): Int {
        return (
            (
                stats.attackPower.coerceAtLeast(0) +
                strength() * character.klass.attackPowerFromStrength +
                agility() * character.klass.attackPowerFromAgility
            ) * stats.attackPowerMultiplier
        ).toInt()
    }

    fun rangedAttackPower(): Int {
        return (
            (
                stats.attackPower.coerceAtLeast(0) +
                agility() * character.klass.rangedAttackPowerFromAgility
            ) * stats.rangedAttackPowerMultiplier
        ).toInt()
    }

    fun spellDamage(): Int {
        return stats.spellDamage
    }

    fun meleeHitPct(): Double {
        return stats.physicalHitRating / Rating.meleeHitPerPct
    }

    fun spellHitPct(): Double {
        return stats.spellHitRating / Rating.spellHitPerPct
    }

    fun expertisePct(): Double {
        return stats.expertiseRating / Rating.expertisePerPct
    }

    fun meleeCritPct(): Double {
        return stats.physicalCritRating / Rating.critPerPct + agility() * character.klass.critPctPerAgility
    }

    fun spellCritPct(): Double {
        // https://wow.gamepedia.com/Spell_critical_strike
        val intPerCrit = 80.0
        val critFromInt = intellect() / intPerCrit
        return stats.spellCritRating / Rating.critPerPct + critFromInt + character.klass.baseSpellCritChance
    }

    fun armorPen(): Int {
        return stats.armorPen.coerceAtLeast(0)
    }

    fun defenseSkill(): Int {
        return floor(stats.defenseRating / Rating.defensePerPoint).toInt()
    }

    fun dodgePct(): Double {
        return character.klass.baseDodgePct + (agility() * character.klass.dodgePctPerAgility) + (stats.dodgeRating / Rating.dodgePerPct) + (0.04 * defenseSkill())
    }

    fun parryPct(): Double {
        return 5.0 + (stats.parryRating / Rating.parryPerPct) + (0.04 * defenseSkill())
    }

    fun blockPct(): Double {
        return 5.0 + (stats.blockRating / Rating.blockPerPct) + (0.04 * defenseSkill())
    }

    fun resiliencePct(): Double {
        return stats.resilienceRating / Rating.resiliencePerPct
    }

    fun meleeHasteMultiplier(): Double {
        return (1.0 + (stats.physicalHasteRating / Rating.hastePerPct / 100.0)) * stats.physicalHasteMultiplier
    }

    fun spellHasteMultiplier(): Double {
        return (1.0 + (stats.spellHasteRating / Rating.hastePerPct / 100.0)) * stats.spellHasteMultiplier
    }

    fun physicalGcd(): Double {
        // Melee haste doesn't affect melee GCD, sadge
        return sim.gcdBaseMs
    }

    fun spellGcd(): Double {
        return (sim.gcdBaseMs / spellHasteMultiplier()).coerceAtLeast(sim.minGcdMs)
    }

    fun totemGcd(): Double {
        // TODO: Confirm this will be the case in Classic TBC at launch
        return 1000.0
    }
}
