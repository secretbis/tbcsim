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
import kotlin.js.JsExport
import character.classes.boss.Boss as BossClass
import character.races.Boss as BossRace

@JsExport
class SimIteration(
    val subject: Character,
    val rotation: Rotation,
    val opts: SimOptions
) {
    val logger = KotlinLogging.logger {}

    // Setup target
    val target: Character = defaultTarget()

    // Stats storage
    var subjectStats: Stats = Stats()
    var targetStats: Stats = Stats()
    val resource: Resource

    val serverTickMs = 3000
    var lastMp5Tick = 0
    var tickNum: Int = 0
    var elapsedTimeMs: Int = 0
    var events: MutableList<Event> = mutableListOf()
    var mhAutoAttack: MeleeBase? = null
    var ohAutoAttack: MeleeBase? = null
    var buffs: MutableMap<String, Buff> = mutableMapOf()
    var debuffs: MutableMap<String, Debuff> = mutableMapOf()

    // Buffs need a place to store state per iteration
    // Store individual data per instance and store shared data per-string (generally the buff name)
    val buffState: MutableMap<String, Buff.State> = mutableMapOf()
    val abilityState: MutableMap<String, Ability.State> = mutableMapOf()
    val sharedAbilityState: MutableMap<String, Ability.State> = mutableMapOf()
    val debuffState: MutableMap<String, Buff.State> = mutableMapOf()
    val procState: MutableMap<Proc, Proc.State> = mutableMapOf()
    val rotationState: MutableMap<Criterion.Type, Criterion.State> = mutableMapOf()

    // GCD/casting state
    var gcdBaseMs: Double = 1500.0
    val minGcdMs: Double = 1000.0
    var gcdEndMs: Int = 0
    var castEndMs: Int = 0
    var castingAbility: Ability? = null
    var mainHandAutoReplacement: Ability? = null

    fun onGcd(): Boolean {
        return elapsedTimeMs < gcdEndMs
    }

    fun isCasting(): Boolean {
        return elapsedTimeMs < castEndMs
    }

    init {
        // Add auto-attack, if allowed
        if(rotation.autoAttack) {
            if(hasMainHandWeapon()) {
                mhAutoAttack = MeleeMainHand()
            }
            if(hasOffHandWeapon()) {
                ohAutoAttack = MeleeOffHand()
            }
        }

        // Collect buffs from class, talents, gear, and etc
        subject.race.buffs(this).forEach { addBuff(it) }
        subject.klass.buffs.forEach { addBuff(it) }
        subject.klass.talents.filter { it.value.currentRank > 0 }.forEach {
            it.value.buffs(this).forEach { buff -> addBuff(buff) }
        }
        subject.gear.buffs().forEach { addBuff(it) }
        rotation.combat(this).forEach { it.buffs(this).forEach { buff -> addBuff(buff) } }

        // Compute initial stats
        recomputeStats()

        // Initialize our subject resource
        resource = Resource(this)

        // Cast any spells flagged in the rotation as precombat
        rotation.castAllPrecombat(this)

        // Recompute after precombat casts
        recomputeStats()
    }

    private fun recomputeStats() {
        subjectStats = computeStats(subject, buffs.values.toList())
        targetStats = computeStats(target, debuffs.values.toList())
    }

    private fun pruneBuffs() {
        val buffsToRemove = buffs.values.filter {
            it.isFinished(this)
        }
        buffsToRemove.forEach {
            it.reset(this)
            logEvent(Event(
                eventType = Event.Type.BUFF_END,
                buff = it
            ))
            buffs.remove(it.name)
        }

        // Compute stats if something about our buffs changed
        if(buffsToRemove.isNotEmpty()) {
            recomputeStats()
        }
    }

    private fun pruneDebuffs() {
        val debuffsToRemove = debuffs.values.filter {
            it.isFinished(this)
        }
        debuffsToRemove.forEach {
            it.reset(this)
            logEvent(Event(
                eventType = Event.Type.DEBUFF_END,
                buff = it
            ))
            debuffs.remove(it.name)
        }

        if(debuffsToRemove.isNotEmpty()) {
            recomputeStats()
        }
    }

    fun tick() {
        // Filter out and reset any expired buffs/debuffs
        pruneBuffs()
        pruneDebuffs()

        // Find and cast next rotation ability
        if(!isCasting()) {
            // If we are not casting, and have an ability queued up, actually cast it
            if(castingAbility != null) {
                castingAbility!!.beforeCast(this)
                castingAbility!!.cast(this)
                castingAbility!!.afterCast(this)

                // Log cast event
                logEvent(
                    Event(
                        eventType = Event.Type.SPELL_CAST,
                        abilityName = castingAbility!!.name
                    )
                )

                // Reset casting state
                castingAbility = null
            } else {
                val rotationAbility = rotation.next(this, onGcd())
                if (rotationAbility != null) {
                    // Set next cast times, and add latency if configured
                    castingAbility = rotationAbility
                    gcdEndMs = elapsedTimeMs + rotationAbility.gcdMs(this) + opts.latencyMs
                    castEndMs = elapsedTimeMs + rotationAbility.castTimeMs(this) + opts.latencyMs
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
                        mhState?.lastAttackTimeMs = elapsedTimeMs
                    } else mhAutoAttack?.cast(this)
                } else if (ohAutoAttack?.available(this) == true) {
                    ohAutoAttack?.cast(this)
                }
            }
        }

        // Check debuffs
        debuffs.values.forEach {
            if(it.shouldTick(this)) {
                it.tick(this)
            }
        }

        // MP5
        if(subject.klass.resourceType == Resource.Type.MANA) {
            if (elapsedTimeMs - lastMp5Tick >= 5000) {
                addResource(subjectStats.manaPer5Seconds, Resource.Type.MANA)
                lastMp5Tick = elapsedTimeMs
            }
        }

        // Fire server tick proc
        if(elapsedTimeMs % serverTickMs == 0) {
            fireProc(listOf(Proc.Trigger.SERVER_TICK), null, null, null)
        }
    }

     fun cleanup() {
         // Log end for all buffs
         buffs.values.forEach {
             logEvent(Event(
                eventType = Event.Type.BUFF_END,
                buff = it
            ))
         }

         debuffs.values.forEach {
             logEvent(Event(
                eventType = Event.Type.DEBUFF_END,
                buff = it
            ))
         }
     }

    fun replaceNextMainHandAutoAttack(ability: Ability) {
        this.mainHandAutoReplacement = ability
    }

    fun addBuff(buff: Buff) {
        buff.refresh(this)

        // If this buff stacks, track stacks
        val stacks = if(buff.maxStacks > 0) {
            buffState[buff.name]?.currentStacks ?: 0
        } else 0

        // If this is a new buff, add it
        val exists = buffs[buff.name] != null
        if(!exists) {
            // If this buff is mutex with others, remove any existing with that class
            if(!buff.mutex.contains(Buff.Mutex.NONE)) {
                // A buff should be removed if it matches any of the incoming buff's mutex categories
                val toRemove = buffs.values.filter { existing -> buff.mutex.any { existing.mutex.contains(it) } }
                toRemove.forEach {
                    it.reset(this)
                    logEvent(Event(
                        eventType = Event.Type.BUFF_END,
                        buff = it
                    ))
                    buffs.remove(it.name)
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

    fun addDebuff(debuff: Debuff) {
        debuff.refresh(this)

        // If this debuff stacks, track stacks
        val stacks = if(debuff.maxStacks > 0) {
            debuffState[debuff.name]?.currentStacks ?: 0
        } else 0

        // If this is a new debuff, add it
        val exists = debuffs[debuff.name] != null
        if(!exists) {
            debuffs[debuff.name] = debuff
            logEvent(Event(
                eventType = Event.Type.DEBUFF_START,
                buff = debuff,
                buffStacks = stacks
            ))

            // Always recompute after adding a debuff
            recomputeStats()
        } else {
            logEvent(Event(
                eventType = Event.Type.DEBUFF_REFRESH,
                buff = debuff,
                buffStacks = stacks
            ))

            // If a debuff is stackable, then recompute on a refresh as well
            if(stacks > 0) {
                recomputeStats()
            }
        }
    }

    fun consumeBuff(buff: Buff) {
        val state = buffState[buff.name]
        if(state != null) {
            state.currentCharges -= 1

            logEvent(Event(
                eventType = Event.Type.BUFF_CHARGE_CONSUMED,
                buff = buff,
                buffStacks = state.currentStacks
            ))

            pruneBuffs()
            pruneDebuffs()
        }
    }

    fun addResource(amount: Int, type: Resource.Type, ability: Ability? = null) {
        if(resource.type == type) {
            resource.add(amount)

            logEvent(Event(
                eventType = Event.Type.RESOURCE_CHANGED,
                amount = resource.currentAmount.toDouble(),
                delta = amount.toDouble(),
                amountPct = resource.currentAmount / resource.maxAmount.toDouble() * 100.0,
                abilityName = ability?.name
            ))
        } else {
            logger.warn { "Attempted to add resource type $type but subject resource is ${resource.type}" }
        }
    }

    fun subtractResource(amount: Int, type: Resource.Type, ability: Ability? = null) {
        if(resource.type == type) {
            resource.subtract(amount)

            logEvent(Event(
                eventType = Event.Type.RESOURCE_CHANGED,
                amount = resource.currentAmount.toDouble(),
                delta = amount.toDouble(),
                amountPct = resource.currentAmount / resource.maxAmount.toDouble() * 100.0,
                abilityName = ability?.name
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
            buffs.values.forEach { buff ->
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

                // Always check buff/debuff state after any proc
                pruneBuffs()
                pruneDebuffs()
            }
        }
    }

    fun logEvent(event: Event) {
        // Auto-set tick and time if not specified
        if(event.tick == -1) {
            event.tick = tickNum
        }

        if(event.timeMs == -1) {
            event.timeMs = elapsedTimeMs
        }

        logger.trace { "Got event: ${event.abilityName} - ${event.tick} (${event.tick * opts.stepMs}ms) - ${event.eventType} - ${event.result} - ${event.amount}" }

        events.add(event)
    }

    private fun defaultTarget(): Character {
        val char = Character(
            BossClass(baseStats = Stats(
                armor = 7700
            )),
            BossRace(),
            opts.targetLevel
        )

        computeStats(char, listOf())

        return char
    }

    fun hasMainHandWeapon(): Boolean {
        return subject.gear.mainHand.id != -1
    }

    fun hasOffHandWeapon(): Boolean {
        return subject.gear.offHand.id != -1
    }

    fun isDualWielding(): Boolean {
        return subject.klass.canDualWield && hasMainHandWeapon() && hasOffHandWeapon()
    }

    fun weaponSpeed(item: Item): Double {
        return (item.speed / meleeHasteMultiplier()).coerceAtLeast(0.01)
    }

    fun isExecutePhase(): Boolean {
        // The last 20% of the duration is considered to be Execute phase
        return elapsedTimeMs >= 0.8 * opts.durationMs
    }

    fun computeStats(character: Character, buffs: List<Buff>): Stats {
        return Stats()
            .add(character.klass.baseStats)
            .add(character.race.baseStats)
            .add(character.gear.totalStats())
            .let {
                buffs.forEach { buff ->
                    val stats = buff.modifyStats(this)
                    if(stats != null) {
                        it.add(stats)
                    }
                }
                it
            }
    }

    fun strength(): Int {
        return (subjectStats.strength.coerceAtLeast(0) * subjectStats.strengthMultiplier).toInt()
    }

    fun agility(): Int {
        return (subjectStats.agility.coerceAtLeast(0) * subjectStats.agilityMultiplier).toInt()
    }

    fun intellect(): Int {
        return (subjectStats.intellect.coerceAtLeast(0) * subjectStats.intellectMultiplier).toInt()
    }

    fun spirit(): Int {
        return (subjectStats.spirit.coerceAtLeast(0) * subjectStats.spiritMultiplier).toInt()
    }

    fun stamina(): Int {
        return (subjectStats.stamina.coerceAtLeast(0) * subjectStats.staminaMultiplier).toInt()
    }

    fun armor(): Int {
        return (subjectStats.armor.coerceAtLeast(0) * subjectStats.armorMultiplier).toInt()
    }

    fun targetArmor(): Int {
        return (targetStats.armor.coerceAtLeast(0) * targetStats.armorMultiplier).toInt()
    }

    fun attackPower(): Int {
        return (
            (
                subjectStats.attackPower.coerceAtLeast(0) +
                strength() * subject.klass.attackPowerFromStrength +
                agility() * subject.klass.attackPowerFromAgility
            ) * subjectStats.attackPowerMultiplier
        ).toInt()
    }

    fun rangedAttackPower(): Int {
        return (
            (
                subjectStats.attackPower.coerceAtLeast(0) +
                agility() * subject.klass.rangedAttackPowerFromAgility
            ) * subjectStats.rangedAttackPowerMultiplier
        ).toInt()
    }

    fun spellDamage(): Int {
        return (subjectStats.spellDamage * subjectStats.spellDamageMultiplier).toInt()
    }

    fun meleeHitPct(): Double {
        return subjectStats.physicalHitRating / Rating.meleeHitPerPct
    }

    fun spellHitPct(): Double {
        return subjectStats.spellHitRating / Rating.spellHitPerPct
    }

    fun expertisePct(): Double {
        return subjectStats.expertiseRating / Rating.expertisePerPct
    }

    fun meleeCritPct(): Double {
        return subjectStats.physicalCritRating / Rating.critPerPct + agility() * subject.klass.critPctPerAgility
    }

    fun spellCritPct(): Double {
        // https://wow.gamepedia.com/Spell_critical_strike
        val intPerCrit = 80.0
        val critFromInt = intellect() / intPerCrit
        return subjectStats.spellCritRating / Rating.critPerPct + critFromInt + subject.klass.baseSpellCritChance
    }

    fun armorPen(): Int {
        return subjectStats.armorPen.coerceAtLeast(0)
    }

    fun meleeHasteMultiplier(): Double {
        return (1.0 + (subjectStats.physicalHasteRating / Rating.hastePerPct / 100.0)) * subjectStats.physicalHasteMultiplier
    }

    fun spellHasteMultiplier(): Double {
        return (1.0 + (subjectStats.physicalHasteRating / Rating.hastePerPct / 100.0)) * subjectStats.spellHasteMultiplier
    }

    fun physicalGcd(): Double {
        return (gcdBaseMs / meleeHasteMultiplier()).coerceAtLeast(minGcdMs)
    }

    fun spellGcd(): Double {
        return (gcdBaseMs / spellHasteMultiplier()).coerceAtLeast(minGcdMs)
    }

    fun totemGcd(): Double {
        // TODO: Confirm this will be the case in Classic TBC at launch
        return 1000.0
    }
}
