package com.gateway.networkparam.repository.dto

import com.gateway.networkparam.entity.enums.MCellConnectionStatus

internal abstract class MCellInfo {

    abstract val isRegistered: Boolean

    abstract val timeStampMillis: Long

    abstract val cellConnectionStatus: MCellConnectionStatus?
}
