package com.gateway.networkparam.entity

import com.gateway.networkparam.entity2.enums.MCellConnectionStatus

internal abstract class MCellInfo {

    abstract val isRegistered: Boolean

    abstract val timeStampMillis: Long

    abstract val cellConnectionStatus: MCellConnectionStatus?
}
