package com.gateway.networkparam.entity.enums

internal enum class ServiceType(val value: String) {
    UNKNOWN("UNKNOWN"),
    VOICE("VOICE"),
    DATA("DATA"),
    SMS("SMS"),
    VIDEO("VIDEO"),
    EMERGENCY("EMERGENCY")
}
