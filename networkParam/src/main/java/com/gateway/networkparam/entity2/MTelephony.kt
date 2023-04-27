package com.gateway.networkparam.entity2

data class MTelephony(
    val networkOperator: String? = null,
    val callState: String? = null,
    val dataNetworkType: String? = null,
    val voiceNetworkType: String? = null,
    val simState: String? = null,
    val serviceState: ServiceState? = null,
    val signalStrength: SignalStrengthLte? = null,
)
