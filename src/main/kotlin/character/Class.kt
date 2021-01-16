package character

interface Class {
    // Everything that comes with the class
    var baseStats: Stats
    var abilities: List<Ability>
    var talents: List<Talent>

    // Class resource
    var resourceType: Resource.Type
//    var resource: Resource
    var baseResourceAmount: Int  // This refers to any base amount inherent to the *class*

    var canDualWield: Boolean
    var isDualWielding: Boolean
    var allowAutoAttack: Boolean
}
