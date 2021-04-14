package sim

import character.*
import character.auto.AutoAttackBase
import character.auto.AutoShot
import character.auto.MeleeMainHand
import character.auto.MeleeOffHand
import character.classes.hunter.Hunter
import character.classes.hunter.pet.HunterPet
import character.classes.hunter.pet.abilities.PetMelee
import data.model.Item
import mechanics.Rating
import mu.KotlinLogging
import sim.rotation.Criterion
import sim.rotation.Rotation
import sim.rotation.Rule
import kotlin.js.JsExport
import kotlin.math.floor

@JsExport
class SimParticipant(val character: Character, val rotation: Rotation, val sim: SimIteration, val owner: SimParticipant? = null, val epStatMod: Stats? = null) {
    val logger = KotlinLogging.logger {}

    var stats: Stats = Stats()
    lateinit var resource: Resource

    var mhAutoAttack: AutoAttackBase? = null
    var ohAutoAttack: AutoAttackBase? = null
    var rangedAutoAttack: AutoShot? = null

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

    // Pet
    val pet: SimParticipant? = if(character.pet != null && character.petRotation != null) {
        SimParticipant(character.pet, character.petRotation, sim, this)
    } else null

    fun init(): SimParticipant {
        // Add auto-attack, if allowed
        if (rotation.autoAttack) {
            if(character.klass is Hunter) {
                rangedAutoAttack = AutoShot()
            } else if(character.klass is HunterPet) {
                mhAutoAttack = PetMelee()
            } else {
                if (hasMainHandWeapon()) {
                    mhAutoAttack = MeleeMainHand()
                }
                if (hasOffHandWeapon()) {
                    ohAutoAttack = MeleeOffHand()
                }
            }
        }

        // Collect buffs from class, talents, gear, and etc
        character.race.buffs(this).forEach { addBuff(it) }
        character.klass.buffs.forEach { addBuff(it) }
        character.klass.talents.filter { it.value.currentRank > 0 }.forEach {
            it.value.buffs(this).forEach { buff -> addBuff(buff) }
        }
        character.gear.buffs().forEach { addBuff(it) }
        rotation.combatAbilities.forEach { it.buffs(this).forEach { buff -> addBuff(buff) } }

        // Compute initial stats
        recomputeStats()

        // Initialize our subject resource
        resource = Resource(this)

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
            .add(epStatMod ?: Stats())
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
        // Find next rotation ability, if we are not currently casting something
        if(!isCasting() && castingRule == null) {
            val rotationRule = rotation.next(this, onGcd())
            val rotationAbility = rotationRule?.ability
            if (rotationAbility != null) {
                // Set next cast times, and add latency if configured
                castingRule = rotationRule
                gcdEndMs = sim.elapsedTimeMs + rotationAbility.gcdMs(this) + sim.opts.latencyMs
                castEndMs = sim.elapsedTimeMs + rotationAbility.castTimeMs(this) + sim.opts.latencyMs
            }
        }

        // Double check isCasting here, in case we just picked an instant cast spell
        // An instant spell is never "casting", as it has a cast time of zero
        // So, make sure to cast it on the same tick to avoid adding artificial latency of <step_size> ms
        if(!isCasting()) {
            // If we are not casting, and have an ability queued up, actually cast it
            if(castingRule != null) {
                // Double check resource, since it could have changed since the start of the attack
                if(castingRule!!.ability.resourceCost(this) <= resource.currentAmount) {
                    castingRule!!.ability.beforeCast(this)
                    castingRule!!.ability.cast(this)
                    castingRule!!.ability.afterCast(this)

                    // Log cast event
                    val castEvent = Event(
                        eventType = Event.Type.SPELL_CAST,
                        abilityName = castingRule!!.ability.name,
                        target = sim.target
                    )
                    logEvent(castEvent)

                    // Fire cast procs
                    fireProc(listOf(Proc.Trigger.SPELL_CAST), null, castingRule!!.ability, castEvent)
                } else {
                    logger.info { "Canceled queued cast of ${castingRule!!.ability.name} - low resource" }
                }

                // Reset casting state
                castingRule = null
            }

            // Do auto attacks
            if(rangedAutoAttack?.available(this) == true) {
                rangedAutoAttack?.cast(this)
            } else if (mhAutoAttack?.available(this) == true) {
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
                    val mhState = mhAutoAttack?.state(this) as AutoAttackBase.AutoAttackState?
                    mhState?.lastAttackTimeMs = sim.elapsedTimeMs
                } else mhAutoAttack?.cast(this)
            } else if (ohAutoAttack?.available(this) == true) {
                ohAutoAttack?.cast(this)
            }
        }
    }

    fun replaceNextMainHandAutoAttack(ability: Ability) {
        this.mainHandAutoReplacement = ability
    }

    // Determine the priority of an incoming mutex buff/debuff against already-present mutex buffs/debuffs of the same type
    private fun shouldApplyBuff(buffDebuff: Buff, buffsDebuffs: Map<String, Buff>): Boolean {
        // If this buff is mutex with others, compare priority and remove the weaker one(s)
        // If they are equal, choose the most recent (this one)
        return if(buffDebuff.mutex.contains(Mutex.NONE)) {
            true
        } else {
            buffDebuff.mutex.map { mutex ->
                val allMutex = buffsDebuffs.values
                    .filter { existing -> buffDebuff.mutex.any { existing.mutex.contains(it) } }

                if(allMutex.isNotEmpty()) {
                    val highestPriority = allMutex.map { it.mutexPriority(this)[mutex] ?: 0 }.last()

                    // If this incoming buff is the highest or equal priority, keep it and remove the others
                    // Otherwise, do not apply the buff
                    val incomingBuffPriority = buffDebuff.mutexPriority(this)[mutex] ?: 0
                    // We should add the buff if it is highest in *any* mutex category
                    incomingBuffPriority >= highestPriority
                } else true
            }.any { it }
        }
    }

    private fun <T : Buff> pruneByPriority(newBuff: T, buffsDebuffs: Map<String, T>, removeDelegate: (List<T>) -> Unit) {
        // Remove all buffs in each mutex category
        newBuff.mutex.forEach { mutex ->
            if(mutex != Mutex.NONE) {
                val toRemove = buffsDebuffs.values.filter { it.mutex.contains(mutex) }
                toRemove.forEach {
                    it.reset(this)
                    removeDelegate(listOf(it))
                }
            }
        }
    }

    // Buffs
    fun addBuff(buff: Buff) {
        // Check mutex
        val shouldAddBuff = shouldApplyBuff(buff, buffs)
        if(shouldAddBuff) {
            pruneByPriority(buff, buffs, ::removeBuffs)
        } else return

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
        // Check mutex
        val shouldApplyDebuff = shouldApplyBuff(debuff, debuffs)
        if(shouldApplyDebuff) {
            pruneByPriority(debuff, debuffs, ::removeDebuffs)
        } else return

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
        if(amount == 0) return

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
            logger.debug { "Attempted to add resource type $type but subject resource is ${resource.type}" }
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
            logger.debug { "Attempted to subtract resource type $type but subject resource is ${resource.type}" }
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
        return (item.speed / physicalHasteMultiplier()).coerceAtLeast(0.01)
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
                strength() * character.klass.attackPowerFromStrength
            ) * stats.attackPowerMultiplier
        ).toInt()
    }

    fun rangedAttackPower(): Int {
        return (
            (
                stats.rangedAttackPower.coerceAtLeast(0) +
                agility() * character.klass.rangedAttackPowerFromAgility
            ) * stats.rangedAttackPowerMultiplier
        ).toInt()
    }

    fun spellDamage(): Int {
        return stats.spellDamage
    }

    fun physicalHitPct(): Double {
        return stats.physicalHitRating / Rating.physicalHitPerPct
    }

    fun spellHitPct(): Double {
        return stats.spellHitRating / Rating.spellHitPerPct
    }

    fun expertisePct(): Double {
        return stats.expertiseRating / Rating.expertisePerPct
    }

    fun meleeCritPct(): Double {
        return stats.meleeCritRating / Rating.critPerPct + agility() * character.klass.critPctPerAgility
    }

    fun rangedCritPct(): Double {
        return stats.rangedCritRating / Rating.critPerPct + agility() * character.klass.critPctPerAgility
    }

    fun spellCritPct(): Double {
        val critFromInt = intellect() * character.klass.spellCritPctPerInt
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

    fun physicalHasteMultiplier(): Double {
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
