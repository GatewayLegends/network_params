package com.gateway.networkparam.entity.enums

internal enum class RegState(val value: String) {
    IN_SERVICE("IN_SERVICE"),
    OUT_OF_SERVICE("OUT_OF_SERVICE"),
    EMERGENCY_ONLY("EMERGENCY_ONLY"),
    POWER_OFF("POWER_OFF")
}
