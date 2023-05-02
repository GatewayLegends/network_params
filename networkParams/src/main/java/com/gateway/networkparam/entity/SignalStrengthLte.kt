package com.gateway.networkparam.entity

data class SignalStrengthLte(
    val rsrp: Int? = null,
    val rsrq: Int? = null,
    val rssi: Int? = null,
    val rssnr: Int? = null,
    val cqi: Int? = null,
    val cqiTableIndex: Int? = null,
    val timingAdvance: Int? = null,
    val dbm: Int? = null,
    val asuLevel: Int? = null,
    val level: Int? = null,
    val miuiLevel: Int? = null,
    val networkOperator: String? = null
)
