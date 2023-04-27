package com.gateway.networkparam.entity2

data class NetworkRegistrationInfo(
    val domain: String? = null,
    val accessNetworkTechnology: String? = null,
    val registeredPlmn: String? = null,
    val availableServices: List<String?> = emptyList(),
    val cellIdentity: ECellIdentityLte? = null,
    val transportType: String? = null,
    val isRegistered: Boolean? = null,
    val isSearching: Boolean? = null,
    val isRoaming: Boolean? = null,
    val configRadioTechnology: Int? = null,
    val nsaState: Int? = null,
    val registrationState: Int? = null,
    val rejectCause: Int? = null,
    val roamingType: Int? = null,
    val isEmergencyEnabled: Boolean? = null
)
