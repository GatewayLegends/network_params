package com.gateway.networkparam.entity2.enums

internal enum class MCellConnectionStatus(val value: String) {
    NONE("NONE"),
    PRIMARY_SERVING("PRIMARY_SERVING"),
    SECONDARY_SERVING("SECONDARY_SERVING"),
    UNKNOWN("UNKNOWN")
}
