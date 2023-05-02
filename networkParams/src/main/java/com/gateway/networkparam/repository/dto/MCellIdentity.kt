package com.gateway.networkparam.repository.dto

import android.telephony.CellInfo

internal abstract class MCellIdentity {

    protected fun Int.orNull() = if (this == CellInfo.UNAVAILABLE) null else this

    protected fun Long.orNull() = if (this == CellInfo.UNAVAILABLE_LONG) null else this
}
