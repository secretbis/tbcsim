package sim

import character.*
import character.auto.MeleeMainHand
import character.auto.MeleeOffHand
import data.model.Item
import mechanics.Rating
import mu.KotlinLogging
import sim.rotation.Rotation
import character.classes.boss.Boss as BossClass
import character.races.Boss as BossRace

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

    val serverTickMs = 2000
    var lastMp5Tick = 0
    var tick: Int = 0
    var elapsedTimeMs: Int = 0
    var events: MutableList<Event> = mutableListOf()
    var autoAttack: List<Ability> = listOf()
    var buffs: MutableList<Buff> = mutableListOf()
    var debuffs: MutableList<Debuff> = mutableListOf()

    // Buffs need a place to store state per iteration
    // Store individual data per instance and store shared data per-string (generally the buff name)
    val buffState: MutableMap<Buff, Buff.State> = mutableMapOf()
    val sharedBuffState: MutableMap<String, Buff.State> = mutableMapOf()
    val abilityState: MutableMap<String, Ability.State> = mutableMapOf()
    val sharedAbilityState: MutableMap<String, Ability.State> = mutableMapOf()
    val debuffState: MutableMap<Buff, Buff.State> = mutableMapOf()
    val sharedDebuffState: MutableMap<String, Buff.State> = mutableMapOf()

    // GCD/casting state
    var gcdBaseMs: Double = 1500.0
    val minGcdMs: Double = 1000.0
    var gcdEndMs: Int = 0
    var castEndMs: Int = 0

    fun onGcd(): Boolean {
        return elapsedTimeMs < gcdEndMs
    }

    fun isCasting(): Boolean {
        return elapsedTimeMs < castEndMs
    }

    init {
        // Add auto-attack, if allowed
        if(subject.klass.allowAutoAttack) {
            autoAttack = listOf(
                MeleeMainHand(),
                MeleeOffHand()
            )
        }

        // Collect buffs from class, talents, gear, and etc
        subject.klass.buffs.forEach { addBuff(it) }
        subject.klass.talents.filter { it.value.currentRank > 0 }.forEach {
            it.value.buffs(this).forEach { buff -> addBuff(buff) }
        }
        subject.gear.buffs().forEach { addBuff(it) }

        // Cast any spells flagged in the rotation as precombat
        rotation.precombat(this)

        // Compute initial stats
        recomputeStats()

        // Initialize our subject resource
        resource = Resource(this)
    }

    private fun recomputeStats() {
        subjectStats = computeStats(subject, buffs)
        targetStats = computeStats(target, debuffs)
    }

    private fun pruneBuffs() {
        val buffsToRemove = buffs.filter {
            it.isFinished(this)
        }
        buffsToRemove.forEach {
            it.reset(this)
            logEvent(Event(
                eventType = Event.Type.BUFF_END,
                buff = it
            ))
        }
        buffs.removeAll(buffsToRemove)

        // Compute stats if something about our buffs changed
        if(buffsToRemove.isNotEmpty()) {
            recomputeStats()
        }
    }

    private fun pruneDebuffs() {
        val debuffsToRemove = debuffs.filter {
            it.isFinished(this)
        }
        debuffsToRemove.forEach {
            it.reset(this)
            logEvent(Event(
                eventType = Event.Type.DEBUFF_END,
                buff = it
            ))
        }
        debuffs.removeAll(debuffsToRemove)

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
            val rotationAbility = rotation.next(this, onGcd())
            if(rotationAbility != null) {
                if(!onGcd() || (onGcd() && rotationAbility.castableOnGcd)) {
                    val resourceCost = rotationAbility.resourceCost(this).toInt()
                    val resourceType = rotationAbility.resourceType
                    if(resourceCost != 0) {
                        subtractResource(resourceCost, resourceType, rotationAbility)
                    }

                    rotationAbility.cast(this)
                    rotationAbility.afterCast(this)

                    // Log cast event
                    // TODO: Is the SPELL_START event useful?
                    logEvent(
                        Event(
                            eventType = Event.Type.SPELL_CAST,
                            abilityName = rotationAbility.name
                        )
                    )

                    // Set next cast times, and add latency if configured
                    gcdEndMs = elapsedTimeMs + rotationAbility.gcdMs(this) + opts.latencyMs
                    castEndMs = elapsedTimeMs + rotationAbility.castTimeMs(this) + opts.latencyMs
                }
            }
        }

        // Do auto attacks
        autoAttack.forEach {
            if(it.available(this)) it.cast(this)
        }

        // Check debuffs
        debuffs.forEach {
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
            fireProc(listOf(Proc.Trigger.SERVER_TICK))
        }
    }

     fun cleanup() {
         // Log end for all buffs
         buffs.forEach {
             logEvent(Event(
                eventType = Event.Type.BUFF_END,
                buff = it
            ))
         }

         debuffs.forEach {
             logEvent(Event(
                eventType = Event.Type.DEBUFF_END,
                buff = it
            ))
         }
     }

    fun addBuff(buff: Buff) {
        buff.refresh(this)

        // If this buff stacks, track stacks
        val stacks = if(buff.maxStacks > 0) {
            buffState[buff]?.currentStacks ?: 0
        } else 0

        // If this is a new buff, add it
        val exists = buffs.find { it === buff } != null
        if(!exists) {
            // If this buff is mutex with others, remove any existing with that class
            if(!buff.mutex.contains(Buff.Mutex.NONE)) {
                // A buff should be removed if it matches any of the incoming buff's mutex categories
                val toRemove = buffs.filter { existing -> buff.mutex.any { existing.mutex.contains(it) } }
                toRemove.forEach {
                    it.reset(this)
                    logEvent(Event(
                        eventType = Event.Type.BUFF_END,
                        buff = it
                    ))
                }
                buffs.removeAll(toRemove)
            }

            buffs.add(buff)
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
            debuffState[debuff]?.currentStacks ?: 0
        } else 0

        // If this is a new debuff, add it
        val exists = debuffs.find { it === debuff } != null
        if(!exists) {
            debuffs.add(debuff)
            logEvent(Event(
                eventType = Event.Type.DEBUFF_START,
                buff = debuff
            ))

            // Always recompute after adding a debuff
            recomputeStats()
        } else {
            logEvent(Event(
                eventType = Event.Type.DEBUFF_REFRESH,
                buff = debuff
            ))

            // If a debuff is stackable, then recompute on a refresh as well
            if(stacks > 0) {
                recomputeStats()
            }
        }
    }

    fun consumeBuff(buff: Buff) {
        val state = buffState[buff]
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

    fun fireProc(triggers: List<Proc.Trigger>, items: List<Item>? = null, ability: Ability? = null) {
        // Collect fireable procs
        val allProcs: MutableSet<Proc> = mutableSetOf()
        for(trigger in triggers) {
            // Get procs from active buffs
            buffs.forEach { buff ->
                buff.procs(this).filter { proc -> proc.triggers.contains(trigger) }.forEach {
                    allProcs.add(it)
                }
            }
        }

        // Fire all found procs
        allProcs.forEach {
            if(it.shouldProc(this, items, ability)) {
                it.proc(this, items, ability)

                // Always check buff/debuff state after any proc
                pruneBuffs()
                pruneDebuffs()
            }
        }
    }

    fun logEvent(event: Event) {
        // Auto-set tick and time if not specified
        if(event.tick == -1) {
            event.tick = tick
        }

        if(event.timeMs == -1) {
            event.timeMs = elapsedTimeMs
        }

        logger.debug { "Got event: ${event.abilityName} - ${event.tick} (${event.tick * opts.stepMs}ms) - ${event.eventType} - ${event.result} - ${event.amount}" }

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
