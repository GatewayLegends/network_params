package com.gateway.networkparam.entity2

data class CellLte(
    val cellConnectionStatus: String? = null,
    val isRegistered: Boolean? = null,
    val timeStamp: Long? = null,
    val cellIdentityLte: ECellIdentityLte? = null,
    val signalStrengthReport: SignalStrengthLte? = null
)
