package com.gateway.networkparam.entity

data class CellLte(
    val cellConnectionStatus: String? = null,
    val isRegistered: Boolean? = null,
    val timeStamp: Long? = null,
    val cellIdentityLte: CellIdentityLte? = null,
    val signalStrengthReport: SignalStrengthLte? = null
)
